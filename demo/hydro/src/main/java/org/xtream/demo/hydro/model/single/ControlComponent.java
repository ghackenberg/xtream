package org.xtream.demo.hydro.model.single;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.demo.hydro.model.Constants;

public class ControlComponent extends org.xtream.demo.hydro.model.ControlComponent
{
	
	// Ports
	
	public Port<Double> hauptkraftwerkDischargeOutput = new Port<>();
	public Port<Double> wehr1DischargeOutput = new Port<>();
	public Port<Double> wehr2DischargeOutput = new Port<>();
	public Port<Double> wehr3DischargeOutput = new Port<>();
	public Port<Double> wehr4DischargeOutput = new Port<>();
	
	// Expressions
	
	public Expression<Double> hauptkraftwerkTurbineDischargeExpression = new TurbineDischargeExpression(hauptkraftwerkTurbineDischargeOutput, hauptkraftwerkDischargeOutput, scenarioPriceInput, Constants.HAUPTKRAFTWERK_TURBINE_DISCHARGE_MAX, Constants.HAUPTKRAFTWERK_WEIR_DISCHARGE_MAX);
	public Expression<Double> wehr1TurbineDischargeExpression = new TurbineDischargeExpression(wehr1TurbineDischargeOutput, wehr1DischargeOutput, scenarioPriceInput, Constants.WEHR1_TURBINE_DISCHARGE_MAX, Constants.WEHR1_WEIR_DISCHARGE_MAX);
	public Expression<Double> wehr2TurbineDischargeExpression = new TurbineDischargeExpression(wehr2TurbineDischargeOutput, wehr2DischargeOutput, scenarioPriceInput, Constants.WEHR2_TURBINE_DISCHARGE_MAX, Constants.WEHR2_WEIR_DISCHARGE_MAX);
	public Expression<Double> wehr3TurbineDischargeExpression = new TurbineDischargeExpression(wehr3TurbineDischargeOutput, wehr3DischargeOutput, scenarioPriceInput, Constants.WEHR3_TURBINE_DISCHARGE_MAX, Constants.WEHR3_WEIR_DISCHARGE_MAX);
	public Expression<Double> wehr4TurbineDischargeExpression = new TurbineDischargeExpression(wehr4TurbineDischargeOutput, wehr4DischargeOutput, scenarioPriceInput, Constants.WEHR4_TURBINE_DISCHARGE_MAX, Constants.WEHR4_WEIR_DISCHARGE_MAX);
	
	public Expression<Double> hauptkraftwerkWeirDischargeExpression = new WeirDischargeExpression(hauptkraftwerkWeirDischargeOutput, hauptkraftwerkDischargeOutput, scenarioPriceInput, Constants.HAUPTKRAFTWERK_TURBINE_DISCHARGE_MAX, Constants.HAUPTKRAFTWERK_WEIR_DISCHARGE_MAX);
	public Expression<Double> wehr1WeirDischargeExpression = new WeirDischargeExpression(wehr1WeirDischargeOutput, wehr1DischargeOutput, scenarioPriceInput, Constants.WEHR1_TURBINE_DISCHARGE_MAX, Constants.WEHR1_WEIR_DISCHARGE_MAX);
	public Expression<Double> wehr2WeirDischargeExpression = new WeirDischargeExpression(wehr2WeirDischargeOutput, wehr2DischargeOutput, scenarioPriceInput, Constants.WEHR2_TURBINE_DISCHARGE_MAX, Constants.WEHR2_WEIR_DISCHARGE_MAX);
	public Expression<Double> wehr3WeirDischargeExpression = new WeirDischargeExpression(wehr3WeirDischargeOutput, wehr3DischargeOutput, scenarioPriceInput, Constants.WEHR3_TURBINE_DISCHARGE_MAX, Constants.WEHR3_WEIR_DISCHARGE_MAX);
	public Expression<Double> wehr4WeirDischargeExpression = new WeirDischargeExpression(wehr4WeirDischargeOutput, wehr4DischargeOutput, scenarioPriceInput, Constants.WEHR4_TURBINE_DISCHARGE_MAX, Constants.WEHR4_WEIR_DISCHARGE_MAX);

}