package in.viveksrivastava.BPMFinder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javazoom.jl.player.Player;

/**
 * @author Vivek Srivastava
*/

public class Main {
	private List<Integer> bpm = null;
	private List<Double> decibel = null;
	private String fileName;
	private OutputStream outputStream;

	/**
	 * 
	 * @param fileName name of the file to test
	 * @param outputStream output stream to write the data
	 */
	protected Main(String fileName, OutputStream outputStream) {
		this.outputStream = outputStream;
		this.fileName = fileName;
	}

	/**
	 * Call calculate after the object of the class has been initialized.
	 * <br /> 
	 * It performs calculations of BPM and dB
	 */
	public void calculate() throws Exception {

		BPMCalc bProcessor = new BPMCalc();
		DecibelCalc dProcessor = new DecibelCalc();
		bProcessor.setSampleSize(1024);

		if (fileName.endsWith(".mp3")) {
			EnergyProcessor output = new EnergyProcessor(bProcessor,
					dProcessor);
			output.setAverageLength(1024);
			Player player = new Player(new FileInputStream(fileName), output);
			player.play();
		} else if (fileName.endsWith(".wav")) {
			WavEnergyProcessor output = new WavEnergyProcessor(bProcessor,
					dProcessor, fileName);
			output.setAverageLength(1024);
			output.start();
		} else {
			throw new Exception("Unsupported file format");
		}
		setBpm(bProcessor.getBPM());
		setDecibel(dProcessor.getDB());

	}

	/**
	 * @return list of bpm values
	 */
	public List<Integer> getBpm() {
		return bpm;
	}

	private void setBpm(List<Integer> bpm) {
		this.bpm = bpm;
	}

	/**
	 * @return list of decibel values
	 */
	public List<Double> getDecibel() {
		return decibel;
	}

	private void setDecibel(List<Double> decibel) {
		this.decibel = decibel;
	}

	/**
	 * This method can be used to write to different type of output streams. 
	 * <br />
	 * For example, it can be used to write to a file, or to a console, or to a network stream based on the requirement
	 * <br />
	 * It writes data in the following format:
	 * <br />
	 * "FileName",[<comma saperated BPM for every 1 sec of file>],[<comma saperated DB for every 1 sec of file>]
	 * <br />
	 * for example, for a small file, this is the response:
	 * <br />
	 * "D:\\android\\android\\sdk\\samples\\android-19\\legacy\\ApiDemos\\res\\raw\\test_cbr.mp3",[0, 120, 60, 60, 60, 120, 120, 0, 120, 60, 180, 120, 120, 120, 120, 240, 180, 0, 0, 0, 240, 180, 120, 60, 240, 60, 60, 120, 60, 0, 60, 120, 120, 180, 180, 120, 180, 240, 120, 0, 0, 60, 60, 180, 0, 0, 120, 0, 60, 120, 60, 180],[76.89513684329705, 79.21053723472554, 77.71708864253333, 77.96334469521614, 77.62913998459122, 78.34248933532938, 78.62758074881751, 77.32568474312968, 79.84496819291307, 78.86944151185233, 78.27974782488334, 78.41698666105992, 79.07688530774631, 78.7301122322118, 79.92281805113036, 78.33802813066954, 78.40306174273755, 79.37647955949947, 78.6638211924596, 79.09112367321745, 78.75300259185666, 78.58778697191929, 79.0948317043692, 78.75832308854515, 79.00521483531462, 77.69264806377296]
	 */
	public void writeToOutputStream() throws IOException {
		if (outputStream != null) {
			outputStream.write(("\"" + fileName + "\"").getBytes());
			outputStream.write(",".toString().getBytes());
			outputStream.write(getBpm().toString().getBytes());
			outputStream.write(",".toString().getBytes());
			outputStream.write(getDecibel().toString().getBytes());
		}
	}
}
