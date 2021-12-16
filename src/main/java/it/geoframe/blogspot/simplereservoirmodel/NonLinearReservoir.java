/*
 * GNU GPL v3 License
 *
 * Copyright 2021 Niccolo` Tubini
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package it.geoframe.blogspot.simplereservoirmodel;


import static org.hortonmachine.gears.libs.modules.HMConstants.isNovalue;

import oms3.annotations.In;
import oms3.annotations.License;
import oms3.annotations.Out;
import oms3.annotations.Role;
import oms3.annotations.Execute;

import java.util.HashMap;

import oms3.annotations.Author;
import oms3.annotations.Description;
import oms3.annotations.Unit;

import it.geoframe.blogspot.numerical.ode.NewtonRaphson;


@Description("Simple reservoir model.")
@Author(name = "Niccolo' Tubini, and Riccardo Rigon", contact = "tubini.niccolo@gmail.com")
@License("General Public License Version 3 (GPLv3)")

public class NonLinearReservoir {

	
	@Description("Rainfall input")
	@Unit("mm")
	@In 
	public HashMap<Integer, double[]> inRain;
	
	@Description("Initial condition for the reservoir")
	@Unit("mm")
	@In
	public double initialCondition;
	
	@Description("Coefficient of Q(S)")
	@Role("Parameter")
	@In
	public double coefficientDischarge;
	
	@Description("Exponent of Q(S)")
	@Role("Parameter")
	@In
	public double exponentDischarge;
	
	@Description("Area of the basin")
	@Role("Parameter")
	@Unit("km2")
	@In
	public double area;
	
	@Description("Timestep of the simulation")
	@Role("Parameter")
	@Unit("minutes")
	@In
	public double timeStepMinutes;
	
	
	
	@Description("Storage")
	@Unit("mm")
	@Out
	public HashMap<Integer, double[]> outStorage = new HashMap<Integer, double[]>();
	
	@Description("Discharge")
	@Unit("m3/s")
	@Out
	public HashMap<Integer, double[]> outDischarge = new HashMap<Integer, double[]>();
	
	@Description("Volume dischage per unit area")
	@Unit("mm")
	@Out
	public HashMap<Integer, double[]> outDischarge_mm = new HashMap<Integer, double[]>();
	
	
	private double rain;
	private double step;
	private double storage;
	private double discharge;
	private ODE ode;
	private NewtonRaphson newtonRaphson;
	
	@Execute
	public void run() {
		
		rain = inRain.get(0)[0];
		if(isNovalue(rain)) rain=0;
		
		if(step==0) {
			
			storage = initialCondition;
			newtonRaphson = new NewtonRaphson();
			ode = new ODE();

		}
		
		ode.set(storage, rain, coefficientDischarge, exponentDischarge);
		storage = newtonRaphson.solve(storage, ode);
		
		discharge = coefficientDischarge*Math.pow(storage, exponentDischarge);
		
		
		outStorage.put(0,new double[] {storage});
		outDischarge.put( 0, new double[] {discharge/1000 * area*Math.pow(10, 6)/(60*timeStepMinutes)} );
		outDischarge_mm.put(0, new double[] {discharge});
		
		step++;
		
	}
	
}
