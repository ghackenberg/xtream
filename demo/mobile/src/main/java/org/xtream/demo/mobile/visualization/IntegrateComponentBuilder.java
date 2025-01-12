package org.xtream.demo.mobile.visualization;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.xtream.demo.mobile.datatypes.Graph;
import org.xtream.demo.mobile.model.SceneComponent;
import org.xtream.demo.mobile.model.root.ModulesContainer;

public class IntegrateComponentBuilder {

	public SceneComponent buildIntegrateComponent (Graph graph, ModulesContainer modules) {
		
		SceneComponent builtIntegrateComponent = null;
		
		try 
		{
			Class<SceneComponent> integrateClass = SceneComponent.class;
			Constructor<SceneComponent> constructor = integrateClass.getConstructor(org.xtream.demo.mobile.datatypes.Graph.class, org.xtream.demo.mobile.model.root.ModulesContainer.class);
			SceneComponent integrate = constructor.newInstance(graph, modules);

			builtIntegrateComponent = integrate;
		} 
		catch (NoSuchMethodException | SecurityException | InvocationTargetException | InstantiationException | IllegalAccessException | IllegalArgumentException e) 
		{
			e.printStackTrace();
		}
		
		return builtIntegrateComponent;
	}
	
}
