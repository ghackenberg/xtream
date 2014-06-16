package org.xtream.core.optimizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.xtream.core.model.Component;
import org.xtream.core.model.annotations.Objective;

public class Engine<T extends Component>
{
	
	public Class<T> type;
	public int processors;
	public List<Thread> threads;
	public List<Worker> workers;
	public List<T> roots;
	public int timepoint;
	
	public Engine(Class<T> type)
	{
		this(type, Runtime.getRuntime().availableProcessors());
	}
	
	public Engine(Class<T> type, int processors)
	{
		this.type = type;
		this.processors = processors;
		
		threads = new ArrayList<>(processors);
		workers = new ArrayList<>(processors);
		roots = new ArrayList<>(processors);
		
		try
		{
			for (int i = 0; i < processors; i++)
			{
				roots.add(i, type.newInstance());
				roots.get(i).init();
			}
		}
		catch (InstantiationException e)
		{
			throw new IllegalStateException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new IllegalStateException(e);
		}
	}
	
	public void run(int duration, int samples, int classes, double randomness, Monitor<T> monitor)
	{
		// Start monitor
		
		monitor.start();
		
		// Prepare initial state
		
		SortedMap<Key, List<State>> previousGroups = new TreeMap<Key, List<State>>();
		
		State best = new State(roots.get(0).portsRecursive.size(), roots.get(0).fieldsRecursive.size());
		
		best.connect(roots.get(0));
		best.save();
		
		List<State> initialGroup = new ArrayList<>();
		
		initialGroup.add(best);
		
		previousGroups.put(new Key(), initialGroup);
		
		// Run optimization
		
		Statistics statistics = new Statistics();
		
		for (timepoint = 0; timepoint < duration; timepoint++)
		{
			// Prepare statistics
			
			statistics.generatedStates = 0;
			statistics.validStates = 0;
			statistics.dominantStates = 0;
			
			// Start threads
			
			statistics.branch = System.currentTimeMillis();
			
			Queue<Key> queue = new LinkedBlockingQueue<>(previousGroups.keySet());
			
			for (int proccessor = 0; proccessor < processors; proccessor++)
			{
				workers.add(proccessor, new Worker(roots.get(proccessor), timepoint, samples, randomness, previousGroups, queue));
				
				threads.add(proccessor, new Thread(workers.get(proccessor)));
				threads.get(proccessor).start();
			}
			
			// All states
			
			List<State> currentStates = new LinkedList<>();
			
			// Join threads
			
			for (int processor = 0; processor < processors; processor++)
			{
				try
				{
					threads.get(processor).join();
					
					statistics.generatedStates += workers.get(processor).generatedCount;
					statistics.validStates += workers.get(processor).validCount;
					
					currentStates.addAll(workers.get(processor).currentStates);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			
			statistics.branch = System.currentTimeMillis() - statistics.branch;
			
			// Calculate bounds
			
			statistics.norm = System.currentTimeMillis();
			
			double[] minEquivalences = new double[roots.get(0).equivalencesRecursive.size()];
			double[] maxEquivalences = new double[roots.get(0).equivalencesRecursive.size()];
			
			for (int i = 0; i < roots.get(0).equivalencesRecursive.size(); i++)
			{
				minEquivalences[i] = Double.MAX_VALUE;
				maxEquivalences[i] = Double.MIN_VALUE;
			}
			
			for (State current : currentStates)
			{
				for (int i = 0; i < roots.get(0).equivalencesRecursive.size(); i++)
				{
					minEquivalences[i] = Math.min(minEquivalences[i], current.get(roots.get(0).equivalencesRecursive.get(i).port, timepoint));
					maxEquivalences[i] = Math.max(maxEquivalences[i], current.get(roots.get(0).equivalencesRecursive.get(i).port, timepoint));
				}
			}
			
			for (int i = 0; i < roots.get(0).equivalencesRecursive.size(); i++)
			{
				if (minEquivalences[i] == maxEquivalences[i])
				{
					minEquivalences[i] = 0.;
					maxEquivalences[i] = 1.;
				}
			}
			
			statistics.norm = System.currentTimeMillis() - statistics.norm;
			
			// Sort groups
			
			// TODO Factorize cluster strategy. Provide uniform and k-mean clustering.
			
			statistics.cluster = System.currentTimeMillis();
			
			SortedMap<Key, List<State>> currentGroups = new TreeMap<Key, List<State>>();
			
			for (State current : currentStates)
			{
				// Group Status
				
				Key currentKey = new Key(roots.get(0), current, minEquivalences, maxEquivalences, classes, timepoint);
				
				List<State> currentGroup = currentGroups.get(currentKey);
				
				if (currentGroup == null)
				{
					currentGroup = new LinkedList<State>();
					
					currentGroups.put(currentKey, currentGroup);
				}
				
				// Check Status
				
				boolean dominant = true;
				
				for (int index = 0; index < currentGroup.size(); index++)
				{
					State alternative = currentGroup.get(index);
					
					Integer difference = current.compareDominanceTo(alternative);
					
					if (difference != null)
					{
						if (difference < 0)
						{
							dominant = false;
							
							break; // do not keep
						}
						else if (difference == 0)
						{
							dominant = false;
							
							break; // do not keep
						}
						else if (difference > 0)
						{
							currentGroup.remove(index--);
							
							continue;
						}
						
						assert false;
					}
				}
				
				// Save Status
				
				if (dominant)
				{
					currentGroup.add(current);
				}
			}
			
			statistics.cluster = System.currentTimeMillis() - statistics.cluster;
			
			// Prepare next iteration
			
			if (currentGroups.size() > 0)
			{
				statistics.sort = System.currentTimeMillis();
				
				previousGroups = new TreeMap<>(currentGroups);
				
				// Sort states
				
				for (Entry<Key, List<State>> previousGroup : previousGroups.entrySet())
				{
					Collections.sort(previousGroup.getValue());
					
					statistics.dominantStates += previousGroup.getValue().size();
				}
				
				statistics.sort = System.currentTimeMillis() - statistics.sort;
				
				// Calculate stats
				
				statistics.stats = System.currentTimeMillis();
				
				statistics.minObjective = Double.MAX_VALUE;
				statistics.avgObjective = 0;
				statistics.maxObjective = Double.MIN_VALUE;

				for (Entry<Key, List<State>> previousGroup : previousGroups.entrySet())
				{
					for (Objective objective : roots.get(0).minObjectivesRecursive)
					{
						for (State state : previousGroup.getValue())
						{
							double currentObjective = state.get(objective.port, timepoint);
							
							statistics.minObjective = Math.min(statistics.minObjective, currentObjective);
							statistics.avgObjective += currentObjective / statistics.dominantStates;
							statistics.maxObjective = Math.max(statistics.maxObjective, currentObjective);
						}
					}
					for (Objective objective : roots.get(0).maxObjectivesRecursive)
					{
						for (State state : previousGroup.getValue())
						{
							double currentObjective = state.get(objective.port, timepoint);
							
							statistics.minObjective = Math.min(statistics.minObjective, currentObjective);
							statistics.avgObjective += currentObjective / statistics.dominantStates;
							statistics.maxObjective = Math.max(statistics.maxObjective, currentObjective);
						}
					}
				}
				
				statistics.stats = System.currentTimeMillis() - statistics.stats;
				
				// Select best
				
				best = previousGroups.get(previousGroups.firstKey()).get(0);
				
				for (Entry<Key, List<State>> entry : previousGroups.entrySet())
				{
					for (Objective objective : roots.get(0).minObjectivesRecursive)
					{
						if (best.get(objective.port, timepoint) > entry.getValue().get(0).get(objective.port, timepoint))
						{
							best = entry.getValue().get(0);
						}
					}
					for (Objective objective : roots.get(0).maxObjectivesRecursive)
					{
						if (best.get(objective.port, timepoint) < entry.getValue().get(0).get(objective.port, timepoint))
						{
							best = entry.getValue().get(0);
						}
					}
				}
				
				best.restore(roots.get(0));
				
				// Print result
				
				monitor.handle(timepoint, statistics, previousGroups, best);
			}
			else
			{
				break; // Stop optimization
			}
		}
		
		best.restore(roots.get(0));
		
		// Stop monitor
		
		monitor.stop();
	}

}
