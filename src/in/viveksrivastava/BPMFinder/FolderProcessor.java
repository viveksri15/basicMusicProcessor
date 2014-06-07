/*
 * References used:
 * 	http://www.gamedev.net/page/resources/_/technical/math-and-physics/beat-detection-algorithms-r1952
 */

package in.viveksrivastava.BPMFinder;

import java.io.File;
import java.io.FilenameFilter;
import java.io.OutputStream;

/**
 * @author Vivek
 */
public class FolderProcessor extends Main {

	private String folderName;
	private OutputStream outputStream;

	public static void main(String[] args) throws Exception {
		FolderProcessor folderProcessor = new FolderProcessor("D:\\music\\Bleach Songs",
				System.out);
		folderProcessor.start();
	}

	@Override
	public java.util.List<Integer> getBpm() {
		return null;
	}

	@Override
	public java.util.List<Double> getDecibel() {
		return null;
	};

	public FolderProcessor(String foldername, OutputStream outputStream) {
		super(null, null);
		this.folderName = foldername;
		this.outputStream = outputStream;
	}

	public void start() {
		File file = new File(folderName);

		if (file.isDirectory()) {

			String[] files = file.list(new FilenameFilter() {

				@Override
				public boolean accept(File arg0, String arg1) {
					if (arg1.endsWith(".mp3") || arg1.endsWith(".wav"))
						return true;
					return false;
				}
			});

			for (String f : files) {
				Main api = new Main(folderName + "\\" + f, outputStream);
				try {
					api.calculate();
					api.writeToOutputStream();
					outputStream.write("\n".getBytes());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}
}