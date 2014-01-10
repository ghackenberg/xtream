package org.xtream.core.optimizer;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.xtream.core.model.Component;
import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Constant;

public class State implements Comparable<State>
{
	
	public Component root;
	
	public int timepoint;
	
	public State previous;
	
	public Map<Port<?>, Object> values = new HashMap<>();
	
	public Map<Port<?>, Map<Field, Object>> fields = new HashMap<>();
	
	public State(Component root)
	{
		this(root, -1, null);
	}
	
	public State(Component root, int timepoint, State parent)
	{
		this.root = root;
		this.timepoint = timepoint;
		this.previous = parent;
		
		for (Port<?> port : root.portsRecursive)
		{
			port.state = this;
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(Port<T> port, int timepoint)
	{
		if (this.timepoint == timepoint)
		{
			return (T) values.get(port);
		}
		else
		{
			return previous.get(port, timepoint);
		}
	}
	
	public <T> void set(Port<T> port, int timepoint, T value)
	{
		if (this.timepoint == timepoint)
		{
			values.put(port, value);
		}
		else
		{
			previous.set(port, timepoint, value);
		}
	}
	
	public void save()
	{
		for (Port<?> port : root.portsRecursive)
		{
			fields.put(port, new HashMap<Field, Object>());
			
			for (Field field : port.getClass().getFields())
			{
				try
				{
					field.setAccessible(true);
					
					if (field.getAnnotation(Constant.class) == null)
					{
						fields.get(port).put(field, field.get(port));
					}
				}
				catch (IllegalArgumentException e)
				{
					e.printStackTrace();
				}
				catch (IllegalAccessException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	public void load()
	{
		for (Port<?> port : root.portsRecursive)
		{
			for (Field field : port.getClass().getFields())
			{
				try
				{
					field.setAccessible(true);
					
					if (field.getAnnotation(Constant.class) == null)
					{
						field.set(port, fields.get(port).get(field));
					}
				}
				catch (IllegalArgumentException e)
				{
					e.printStackTrace();
				}
				catch (IllegalAccessException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	public void dump()
	{
		for (Entry<Port<?>, Map<Field, Object>> port : fields.entrySet())
		{
			for (Entry<Field, Object> value : port.getValue().entrySet())
			{
				System.out.println(port.getKey().name + "." + value.getKey().getName() + " = " + value.getValue());
			}
		}
	}
	
	public Integer compareDominanceTo(State other)
	{
		if (root.minDominancesRecursive.size() > 0 || root.maxDominancesRecursive.size() > 0)
		{
			// Check equivalence
			
			for (Port<?> port : root.equivalences)
			{
				if (!get(port, timepoint).equals(other.get(port, timepoint)))
				{
					return null; // Not comparable
				}
			}
		
			// Check min dominance
			
			double difference = 0;
			
			for (Port<Double> port : root.minDominancesRecursive)
			{
				double temp = get(port, timepoint) - other.get(port, timepoint);
				
				if (difference != 0 && Math.signum(difference) != Math.signum(temp))
				{
					return null; // Not comparable
				}
			}
			
			// Check max dominance
			
			difference *= -1;
			
			for (Port<Double> port : root.maxDominancesRecursive)
			{
				double temp= get(port, timepoint) - other.get(port, timepoint);
				
				if (difference != 0 && Math.signum(difference) != Math.signum(temp))
				{
					return null; // Not comparable
				}
			}
			
			// Return result
			
			return (int) Math.signum(difference);
		}
		else
		{
			return null; // Not comparable
		}
	}

	@Override
	public int compareTo(State other)
	{
		if (root.minObjectivesRecursive.size() == 1 || root.maxObjectivesRecursive.size() == 1)
		{
			for (Port<Double> port : root.minObjectivesRecursive)
			{
				return (int) Math.signum(get(port, timepoint) - other.get(port, timepoint));
			}
			for (Port<Double> port : root.maxObjectivesRecursive)
			{
				return (int) Math.signum(get(port, timepoint) - other.get(port, timepoint)) * -1;
			}
			
			throw new IllegalStateException();
		}
		else
		{
			return 0;
		}
	}

}
