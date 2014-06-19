package org.xtream.demo.hydro.model.single.continuous.backward;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.expressions.ConstantExpression;
import org.xtream.demo.hydro.model.Constants;

public class ControlComponent extends org.xtream.demo.hydro.model.single.ControlComponent
{
	
	// Ports
	
	public Port<Double> volumen5LevelInput = new Port<>(); // Dummy port
	public Port<Double> wehr5DischargeOutput = new Port<>(); // Dummy port
	
	// Expressions
	
	public Expression<Double> volumen5LevelExpression = new ConstantExpression<>(volumen5LevelInput, 0.);
	
	public Expression<Double> hauptkraftwerkDischargeExpression = new DischargeExpression(hauptkraftwerkDischargeOutput, Constants.HAUPTKRAFTWERK_DISCHARGE_MAX, volumen1LevelInput, Constants.VOLUMEN1_AREA, Constants.VOLUMEN1_LEVEL_MAX, wehr1DischargeOutput);
	public Expression<Double> wehr1DischargeExpression = new DischargeExpression(wehr1DischargeOutput, Constants.WEHR1_DISCHARGE_MAX, volumen2LevelInput, Constants.VOLUMEN2_AREA, Constants.VOLUMEN2_LEVEL_MAX, wehr2DischargeOutput);
	public Expression<Double> wehr2DischargeExpression = new DischargeExpression(wehr2DischargeOutput, Constants.WEHR2_DISCHARGE_MAX, volumen3LevelInput, Constants.VOLUMEN3_AREA, Constants.VOLUMEN3_LEVEL_MAX, wehr3DischargeOutput);
	public Expression<Double> wehr3DischargeExpression = new DischargeExpression(wehr3DischargeOutput, Constants.WEHR3_DISCHARGE_MAX, volumen4LevelInput, Constants.VOLUMEN4_AREA, Constants.VOLUMEN4_LEVEL_MAX, wehr4DischargeOutput);
	public Expression<Double> wehr4DischargeExpression = new DischargeExpression(wehr4DischargeOutput, Constants.WEHR4_DISCHARGE_MAX, volumen5LevelInput, Constants.VOLUMEN5_AREA, Constants.VOLUMEN5_LEVEL_MAX, wehr5DischargeOutput);
	public Expression<Double> wehr5DischargeExpression = new ConstantExpression<>(wehr5DischargeOutput, 0.);

}