package org.xtream.demo.ppu.model;

import java.awt.Color;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.components.nodes.shapes.CubeComponent;
import org.xtream.core.model.components.transforms.chains.RotationComponent;
import org.xtream.core.model.components.transforms.chains.ScaleComponent;
import org.xtream.core.model.components.transforms.chains.TranslationComponent;
import org.xtream.core.model.components.transforms.chains.rotations.XRotationComponent;
import org.xtream.core.model.components.transforms.chains.rotations.YRotationComponent;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.model.expressions.ConstantExpression;

@SuppressWarnings("unused")
public class StampComponent extends Component
{
	
	// Ports
	
	public Port<RealMatrix> transformInput = new Port<>();
	public Port<RealVector> positionInput = new Port<>();
	public Port<Integer> typeInput = new Port<>();
	public Port<Integer> stateInput = new Port<>();
	
	// Components

	public CylinderComponent cylinder_one = new CylinderComponent(1.0, 2.0, 0.5, 2.0, 0.2, 0.1);
	public CylinderComponent cylinder_two = new CylinderComponent(1.0, 2.0, 0.5, 2.0, 0.2, 0.1);
	
	public SensorComponent sensor_one = new SensorComponent();
	public SensorComponent sensor_two = new SensorComponent();
	
	public CubeComponent cube = new CubeComponent()
	{
		public Expression<Double> sizeExpression = new ConstantExpression<Double>(sizeOutput, 1.0);
		public Expression<Color> colorExpression = new ConstantExpression<>(colorOutput, new Color(0,0,255));
	};
	
	public ScaleComponent scale = new ScaleComponent()
	{
		public Expression<Double> xExpression = new ConstantExpression<>(xOutput, 6.0);
		public Expression<Double> yExpression = new ConstantExpression<>(yOutput, 2.0);
		public Expression<Double> zExpression = new ConstantExpression<>(zOutput, 2.0);
	};
	public TranslationComponent translation_cylinder_one = new TranslationComponent()
	{
		public Expression<Double> xExpression = new ConstantExpression<>(xOutput, 3.0);
		public Expression<Double> yExpression = new ConstantExpression<>(yOutput, 2.0);
		public Expression<Double> zExpression = new ConstantExpression<>(zOutput, 0.0);
	}; 
	public TranslationComponent translation_cylinder_two = new TranslationComponent()
	{
		public Expression<Double> xExpression = new ConstantExpression<>(xOutput, 0.0);
		public Expression<Double> yExpression = new ConstantExpression<>(yOutput, 6.0);
		public Expression<Double> zExpression = new ConstantExpression<>(zOutput, 0.0);
	}; 
	public TranslationComponent translation_sensor_one = new TranslationComponent()
	{
		public Expression<Double> xExpression = new ConstantExpression<>(xOutput, -2.0);
		public Expression<Double> yExpression = new ConstantExpression<>(yOutput, 1.0);
		public Expression<Double> zExpression = new ConstantExpression<>(zOutput, 0.0);
	}; 
	public TranslationComponent translation_sensor_two = new TranslationComponent()
	{
		public Expression<Double> xExpression = new ConstantExpression<>(xOutput, 0.0);
		public Expression<Double> yExpression = new ConstantExpression<>(yOutput, 1.0);
		public Expression<Double> zExpression = new ConstantExpression<>(zOutput, 0.0);
	}; 
	public RotationComponent rotation_one = new YRotationComponent()
	{
		public Expression<Double> angleExpression = new ConstantExpression<>(angleOutput, - Math.PI / 2);
	};
	public RotationComponent rotation_two = new XRotationComponent()
	{
		public Expression<Double> angleExpression = new ConstantExpression<>(angleOutput, Math.PI / 2);
	};
	
	// Channels
	
	public Expression<RealMatrix> transformToScale = new ChannelExpression<>(scale.transformInput, transformInput);
	public Expression<RealMatrix> transformToTranslationCylinderOne = new ChannelExpression<>(translation_cylinder_one.transformInput, transformInput);
	public Expression<RealMatrix> transformToTranslationCylinderTwo = new ChannelExpression<>(translation_cylinder_two.transformInput, transformInput);
	public Expression<RealMatrix> transformToTranslationSensorOne = new ChannelExpression<>(translation_sensor_one.transformInput, transformInput);
	public Expression<RealMatrix> transformToTranslationSensorTwo = new ChannelExpression<>(translation_sensor_two.transformInput, transformInput);
			
	public Expression<RealMatrix> transformScaleToCube = new ChannelExpression<>(cube.transformInput, scale.transformOutput);
	public Expression<RealMatrix> transformTranslationCylinderOneToRotationOne = new ChannelExpression<>(rotation_one.transformInput, translation_cylinder_one.transformOutput);
	public Expression<RealMatrix> transformTranslationCylinderTwoToRotationTwo = new ChannelExpression<>(rotation_two.transformInput, translation_cylinder_two.transformOutput);
	public Expression<RealMatrix> transformTranslationSensorOneToSensorOne = new ChannelExpression<>(sensor_one.transformInput, translation_sensor_one.transformOutput);
	public Expression<RealMatrix> transformTranslationSensorTwoToSensorTwo = new ChannelExpression<>(sensor_two.transformInput, translation_sensor_two.transformOutput);
	public Expression<RealMatrix> transformRotationOneToCylinderOne = new ChannelExpression<>(cylinder_one.transformInput, rotation_one.transformOutput);
	public Expression<RealMatrix> transformRotationTwoToCylinderTwo = new ChannelExpression<>(cylinder_two.transformInput, rotation_two.transformOutput);
	
	public Expression<RealVector> positionToSensorOne = new ChannelExpression<>(sensor_one.positionInput, positionInput);
	public Expression<RealVector> positionToSensorTwo = new ChannelExpression<>(sensor_two.positionInput, positionInput);
	public Expression<Integer> typeToSensorOne = new ChannelExpression<>(sensor_one.typeInput, typeInput);
	public Expression<Integer> typeToSensorTwo = new ChannelExpression<>(sensor_two.typeInput, typeInput);
	public Expression<Integer> stateToSensorOne = new ChannelExpression<>(sensor_one.stateInput, stateInput);
	public Expression<Integer> stateToSensorTwo = new ChannelExpression<>(sensor_two.stateInput, stateInput);
	
	// Expressions
	
	public Expression<Double> energyOneExpression = new Expression<Double>(cylinder_one.energyInput, true)
	{
		protected Double evaluate(State state, int timepoint)
		{
			return Math.random() < 0.5 ? 0.0 : 1.0;
		}
	};
	public Expression<Double> energyTwoExpression = new Expression<Double>(cylinder_two.energyInput, true)
	{
		protected Double evaluate(State state, int timepoint)
		{
			return Math.random() < 0.5 ? 0.0 : 1.0;
		}
	};

}
