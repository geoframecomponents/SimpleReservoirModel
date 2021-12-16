package it.geoframe.blogspot.simplereservoirmodel;

import it.geoframe.blogspot.numerical.ode.OrdinaryDifferentialEquation;

public class ODE implements OrdinaryDifferentialEquation{

	private double initialCondition;
	private double rainfall;
	private double coefficientDischarge;
	private double exponentDischarge;
	
	public void set(double initialCondition, double rainfall, double coefficientDischarge, double exponentDischarge) {
		
		this.initialCondition = initialCondition;
		this.rainfall = rainfall;
		this.coefficientDischarge = coefficientDischarge;
		this.exponentDischarge = exponentDischarge;
	}
	
	@Override
	public double compute(double storage) {
		// TODO Auto-generated method stub
		return storage + coefficientDischarge*Math.pow(storage, exponentDischarge) - initialCondition - rainfall;
	}

	@Override
	public double computeDerivative(double storage) {
		// TODO Auto-generated method stub
		return 1 + exponentDischarge*coefficientDischarge*Math.pow(storage, exponentDischarge-1);
	}

	@Override
	public double computeP(double x) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double computePIntegral(double x) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double computeRHS() {
		// TODO Auto-generated method stub
		return 0;
	}

}
