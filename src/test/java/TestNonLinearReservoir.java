
import java.io.IOException;

import org.hortonmachine.gears.io.timedependent.OmsTimeSeriesIteratorReader;
import org.hortonmachine.gears.io.timedependent.OmsTimeSeriesIteratorWriter;
import org.junit.Test;

import it.geoframe.blogspot.simplereservoirmodel.NonLinearReservoir;

public class TestNonLinearReservoir {

	
	@Test
	public void testNonLinearReservoir() throws IOException {
		
		String startDate = "1994-01-01 00:00";
		String endDate = "1995-01-01 00:00";
		int timeStepMinutes = 60;
		String id = "ID";
		
		String rainfallPath = "resources/Input/rainfall.csv";

		
		OmsTimeSeriesIteratorReader readerRain = new OmsTimeSeriesIteratorReader();
		readerRain.file = rainfallPath;
		readerRain.tStart = startDate;
		readerRain.tEnd = endDate;
		readerRain.tTimestep = timeStepMinutes;
		readerRain.idfield = id;
		readerRain.doProcess = true;
		
		
		String storagePath = "resources/Output/storage.csv";
		String dischargePath = "resources/Output/discharge.csv";
		String discharge_mmPath = "resources/Output/discharge_mm.csv";
		
		OmsTimeSeriesIteratorWriter writerStorage = new OmsTimeSeriesIteratorWriter();
		writerStorage.file = storagePath;
		writerStorage.tStart = startDate;
		writerStorage.tTimestep = timeStepMinutes;
		writerStorage.fileNovalue = "-9999";
		
		OmsTimeSeriesIteratorWriter writerDischarge = new OmsTimeSeriesIteratorWriter();
		writerDischarge.file = dischargePath;
		writerDischarge.tStart = startDate;
		writerDischarge.tTimestep = timeStepMinutes;
		writerDischarge.fileNovalue = "-9999";
		
		OmsTimeSeriesIteratorWriter writerDischarge_mm = new OmsTimeSeriesIteratorWriter();
		writerDischarge_mm.file = discharge_mmPath;
		writerDischarge_mm.tStart = startDate;
		writerDischarge_mm.tTimestep = timeStepMinutes;
		writerDischarge_mm.fileNovalue = "-9999";
		
		
		NonLinearReservoir reservoir = new NonLinearReservoir();
		reservoir.area = 100;
		reservoir.coefficientDischarge = 0.01;
		reservoir.exponentDischarge = 1.3;
		reservoir.timeStepMinutes = timeStepMinutes;
		
		reservoir.initialCondition = 0;
		
		while( readerRain.doProcess ) {
			
			readerRain.nextRecord();
			
			reservoir.inRain = readerRain.outData;
			
			reservoir.run();
			
			writerStorage.inData = reservoir.outStorage;
			writerStorage.writeNextLine();
			
			writerDischarge.inData = reservoir.outDischarge;
			writerDischarge.writeNextLine();
			
			writerDischarge_mm.inData = reservoir.outDischarge_mm;
			writerDischarge_mm.writeNextLine();
			
		}
		
		writerStorage.close();
		writerDischarge_mm.close();
		writerDischarge.close();
	}
	
}
