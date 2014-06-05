package org.xtream.demo.hydro.model.split.continuous.backward;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.expressions.ConstantExpression;
import org.xtream.demo.hydro.model.Constants;

public class ControlComponent extends org.xtream.demo.hydro.model.split.continuous.ControlComponent
{
	
	// Ports
	
	public Port<Double> volumen5LevelInput = new Port<>(); // Dummy port
	public Port<Double> wehr5TurbineDischargeOutput = new Port<>(); // Dummy port
	public Port<Double> wehr5WeirDischargeOutput = new Port<>(); // Dummy port
	
	// Expressions
	
	public Expression<Double> volumen5LevelExpression = new ConstantExpression<>(volumen5LevelInput, 0.);
	
	public Expression<Double> hauptkraftwerkTurbineDischargeExpression = new TurbineDischargeExpression(hauptkraftwerkTurbineDischargeOutput, Constants.HAUPTKRAFTWERK_TURBINE_DISCHARGE_MAX, volumen1LevelInput, Constants.VOLUMEN1_AREA, Constants.VOLUMEN1_LEVEL_MAX, wehr1TurbineDischargeOutput, wehr1WeirDischargeOutput);
	public Expression<Double> wehr1TurbineDischargeExpression = new TurbineDischargeExpression(wehr1TurbineDischargeOutput, Constants.WEHR1_TURBINE_DISCHARGE_MAX, volumen2LevelInput, Constants.VOLUMEN2_AREA, Constants.VOLUMEN2_LEVEL_MAX, wehr2TurbineDischargeOutput, wehr2WeirDischargeOutput);
	public Expression<Double> wehr2TurbineDischargeExpression = new TurbineDischargeExpression(wehr2TurbineDischargeOutput, Constants.WEHR2_TURBINE_DISCHARGE_MAX, volumen3LevelInput, Constants.VOLUMEN3_AREA, Constants.VOLUMEN3_LEVEL_MAX, wehr3TurbineDischargeOutput, wehr3WeirDischargeOutput);
	public Expression<Double> wehr3TurbineDischargeExpression = new TurbineDischargeExpression(wehr3TurbineDischargeOutput, Constants.WEHR3_TURBINE_DISCHARGE_MAX, volumen4LevelInput, Constants.VOLUMEN4_AREA, Constants.VOLUMEN4_LEVEL_MAX, wehr4TurbineDischargeOutput, wehr4WeirDischargeOutput);
	public Expression<Double> wehr4TurbineDischargeExpression = new TurbineDischargeExpression(wehr4TurbineDischargeOutput, Constants.WEHR4_TURBINE_DISCHARGE_MAX, volumen5LevelInput, Constants.VOLUMEN5_AREA, Constants.VOLUMEN5_LEVEL_MAX, wehr5TurbineDischargeOutput, wehr5WeirDischargeOutput);
	public Expression<Double> wehr5TurbineDischargeExpression = new ConstantExpression<>(wehr5TurbineDischargeOutput, 0.);
	
	public Expression<Double> hauptkraftwerkWeirDischargeExpression = new WeirDischargeExpression(hauptkraftwerkWeirDischargeOutput, Constants.HAUPTKRAFTWERK_WEIR_DISCHARGE_MAX, volumen1LevelInput, Constants.VOLUMEN1_AREA, Constants.VOLUMEN1_LEVEL_MAX, wehr1TurbineDischargeOutput, wehr1WeirDischargeOutput, hauptkraftwerkTurbineDischargeOutput);
	public Expression<Double> wehr1WeirDischargeExpression = new WeirDischargeExpression(wehr1WeirDischargeOutput, Constants.WEHR1_WEIR_DISCHARGE_MAX, volumen2LevelInput, Constants.VOLUMEN2_AREA, Constants.VOLUMEN2_LEVEL_MAX, wehr2TurbineDischargeOutput, wehr2WeirDischargeOutput, wehr1TurbineDischargeOutput);
	public Expression<Double> wehr2WeirDischargeExpression = new WeirDischargeExpression(wehr2WeirDischargeOutput, Constants.WEHR2_WEIR_DISCHARGE_MAX, volumen3LevelInput, Constants.VOLUMEN3_AREA, Constants.VOLUMEN3_LEVEL_MAX, wehr3TurbineDischargeOutput, wehr3WeirDischargeOutput, wehr2TurbineDischargeOutput);
	public Expression<Double> wehr3WeirDischargeExpression = new WeirDischargeExpression(wehr3WeirDischargeOutput, Constants.WEHR3_WEIR_DISCHARGE_MAX, volumen4LevelInput, Constants.VOLUMEN4_AREA, Constants.VOLUMEN4_LEVEL_MAX, wehr4TurbineDischargeOutput, wehr4WeirDischargeOutput, wehr3TurbineDischargeOutput);
	public Expression<Double> wehr4WeirDischargeExpression = new WeirDischargeExpression(wehr4WeirDischargeOutput, Constants.WEHR4_WEIR_DISCHARGE_MAX, volumen5LevelInput, Constants.VOLUMEN5_AREA, Constants.VOLUMEN5_LEVEL_MAX, wehr5TurbineDischargeOutput, wehr5WeirDischargeOutput, wehr4TurbineDischargeOutput);
	public Expression<Double> wehr5WeirDischargeExpression = new ConstantExpression<>(wehr5WeirDischargeOutput, 0.);

}