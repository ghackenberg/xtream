package org.xtream.core.model;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.xtream.core.model.annotations.Constant;
import org.xtream.core.model.annotations.Constraint;
import org.xtream.core.model.annotations.Dominance;
import org.xtream.core.model.annotations.Equivalence;
import org.xtream.core.model.annotations.Objective;

public abstract class Component
{
	
	public String name;
	
	public List<Port<?>> ports = new ArrayList<>();
	public List<Field> fields = new ArrayList<>();
	public List<Component> components = new ArrayList<>();
	public List<Channel<?>> channels = new ArrayList<>();
	public List<Port<Boolean>> constraints= new ArrayList<>();
	public List<Port<Double>> minDominances= new ArrayList<>();
	public List<Port<Double>> maxDominances= new ArrayList<>();
	public List<Port<?>> equivalences= new ArrayList<>();
	public List<Port<Double>> minObjectives= new ArrayList<>();
	public List<Port<Double>> maxObjectives= new ArrayList<>();
	
	public List<Port<?>> portsRecursive = new ArrayList<>();
	public List<Field> fieldsRecursive = new ArrayList<>();
	public List<Component> componentsRecursive = new ArrayList<>();
	public List<Channel<?>> channelsRecursive = new ArrayList<>();
	public List<Port<Boolean>> constraintsRecursive= new ArrayList<>();
	public List<Port<Double>> minDominancesRecursive = new ArrayList<>();
	public List<Port<Double>> maxDominancesRecursive = new ArrayList<>();
	public List<Port<?>> equivalencesRecursive= new ArrayList<>();
	public List<Port<Double>> minObjectivesRecursive= new ArrayList<>();
	public List<Port<Double>> maxObjectivesRecursive= new ArrayList<>();
	
	public void init()
	{
		init("root", 0);
	}
	
	@SuppressWarnings("unchecked")
	public void init(String name, int number)
	{
		this.name = name;
		
		for (Field componentField : this.getClass().getFields())
		{
			try
			{
				if (Port.class.isAssignableFrom(componentField.getType()))
				{
					Port<?> port = (Port<?>) componentField.get(this);
					
					ports.add(port);
					
					port.name = name + "." + componentField.getName();
					port.number = number++;
					
					for (Field portField : port.getClass().getFields())
					{
						if (portField.getAnnotation(Constant.class) == null)
						{
							fields.add(portField);
						}
					}
					
					if (componentField.getAnnotation(Constraint.class) != null && (Port<Boolean>) port != null)
					{
						constraints.add((Port<Boolean>) port);
					}
					else if (componentField.getAnnotation(Dominance.class) != null && (Port<Double>) port != null)
					{
						if (componentField.getAnnotation(Dominance.class).value() == Dominance.Value.MAX)
						{
							maxDominances.add((Port<Double>) port);
						}
						else
						{
							minDominances.add((Port<Double>) port);	
						}
					}
					else if (componentField.getAnnotation(Equivalence.class) != null)
					{
						equivalences.add((Port<?>) port);
					}
					else if (componentField.getAnnotation(Objective.class) != null && (Port<Double>) port != null)
					{
						if (componentField.getAnnotation(Objective.class).value() == Objective.Value.MAX)
						{
							maxObjectives.add((Port<Double>) port);
						}
						else
						{
							minObjectives.add((Port<Double>) port);
						}
					}
				}
				else if (Component.class.isAssignableFrom(componentField.getType()))
				{
					Component component = (Component) componentField.get(this);
					
					components.add(component);
					
					component.init(name + "." + componentField.getName(), number);
					
					portsRecursive.addAll(component.portsRecursive);
					fieldsRecursive.addAll(component.fieldsRecursive);
					componentsRecursive.addAll(component.componentsRecursive);
					channelsRecursive.addAll(component.channelsRecursive);
					constraintsRecursive.addAll(component.constraintsRecursive);
					minDominancesRecursive.addAll(component.minDominancesRecursive);
					maxDominancesRecursive.addAll(component.maxDominancesRecursive);
					equivalencesRecursive.addAll(component.equivalencesRecursive);
					minObjectivesRecursive.addAll(component.minObjectivesRecursive);
					maxObjectivesRecursive.addAll(component.maxObjectivesRecursive);
					
					number += component.portsRecursive.size();
				}
				else if (Channel.class.isAssignableFrom(componentField.getType()))
				{
					Channel<?> channel = (Channel<?>) componentField.get(this);
					
					channels.add(channel);
					
					channel.name = name + "." + componentField.getName();
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
		
		portsRecursive.addAll(ports);
		fieldsRecursive.addAll(fields);
		componentsRecursive.addAll(components);
		channelsRecursive.addAll(channels);
		constraintsRecursive.addAll(constraints);
		minDominancesRecursive.addAll(minDominances);
		maxDominancesRecursive.addAll(maxDominances);
		equivalencesRecursive.addAll(equivalences);
		minObjectivesRecursive.addAll(minObjectives);
		maxObjectivesRecursive.addAll(maxObjectives);
	}
	
	public void dump(PrintStream out)
	{
		dump(out, 0);
	}
	
	private void dump(PrintStream out, int indent)
	{
		tabs(out, indent);
		
		out.println(name);
		
		for (Component component : components)
		{
			component.dump(out, indent + 1);
		}
	}
	
	private void tabs(PrintStream out, int indent)
	{
		for (int i = 0; i < indent; i++)
		{
			out.print("\t");
		}
	}

}
