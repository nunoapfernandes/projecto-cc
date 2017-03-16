import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

//import org.tritonus.share.sampled.file.TAudioFileFormat;

@SuppressWarnings("unchecked")
public class Musica {
	private String musica;
	private File musicFile;
	private SourceDataLine sDLine;
	private AudioInputStream aIOStream;
	private Thread aux;
	private boolean flag;

	public Musica() throws UnsupportedAudioFileException, IOException,
			LineUnavailableException {
		// Get music file name
		//this.music =  D:\Music\Trance\DJ P4blu - Groovy On [Intense].mp3
		this.musica = "D:\\08. I Am a River.wav";
		musicFile = new File(musica);
		create();
		aux = new Thread(new Runnable() {
			public void run() {
			}
		});
	}

	/**
	 * Get the Inputs from the music file name
	 */
	private void create() throws UnsupportedAudioFileException, IOException,
			LineUnavailableException {
		AudioInputStream in = AudioSystem.getAudioInputStream(musicFile);
		AudioInputStream din = null;
		if (in != null) {
			AudioFormat baseFormat = in.getFormat();
			AudioFormat decodedFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED,
					baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
					baseFormat.getChannels() * 2, baseFormat.getSampleRate(),
					false);
			din = AudioSystem.getAudioInputStream(decodedFormat, in);
			sDLine = getLine(decodedFormat);
			aIOStream = din;
		}
	}

	/**
	 * Start or continue the music
	 * 
	 * @param t
	 *            A auxiliary thread when play the music keep running the
	 *            program
	 */
	public void play() throws IOException, LineUnavailableException {
		start();
		aux.interrupt();
		aux = new Thread(new Runnable() {
			public void run() {
				rawplay();
			}
		});
		aux.start();
	}

	/**
	 * Play same part of the music
	 */
	private void play(byte[] data) throws IOException {
		try {
			// Read a stream
			int length = aIOStream.read(data, 0, data.length);
			// Play the stream
			sDLine.write(data, 0, length);
		} catch (IOException e) {
			throw e;
		}
	}

	/**
	 * Start the SourceDataLine for play
	 */
	private void start() {
		sDLine.start();
		flag = true;
	}

	/**
	 * The engine of the player
	 */
	private void rawplay() {
		byte[] data = new byte[4096];
		try {
			while (flag)
				play(data);
		} catch (IOException e) {
			System.out.println("Music end");
		}
	}

	/**
	 * Close all I/O
	 */
	public void close() {
		aux.interrupt();
		sDLine.drain();
		sDLine.stop();
		sDLine.close();
		try {
			aIOStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Stop the SourceDataLine, if started again only can continue.
	 */
	public void stop() {
		aux.interrupt();
		sDLine.stop();
		sDLine.drain();
		flag = false;
	}

	/**
	 * If the SourceDataLine is already in stop continue playing.
	 */
	public void resume() {
		sDLine.start();
		flag = true;
		try {
			play();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the SourceDataLine for Read and Write the music
	 * 
	 * @param audioFormat
	 *            The music as Stream
	 */
	private SourceDataLine getLine(AudioFormat audioFormat)
			throws LineUnavailableException {
		SourceDataLine res = null;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class,
				audioFormat);
		res = (SourceDataLine) AudioSystem.getLine(info);
		res.open(audioFormat);
		return res;
	}

	public String getNomeMusica() throws UnsupportedAudioFileException,
			IOException {

		AudioFileFormat baseFileFormat = AudioSystem
				.getAudioFileFormat(musicFile);
		Map<String, Object> propriedade = ((AudioFileFormat) baseFileFormat)
				.properties();

		String nome = (String) propriedade.get("author");
		nome += " - " + propriedade.get("title");

		return nome;
	}

	public String getExtensao() {
		StringBuilder sb = new StringBuilder();
		for (int i = (musica.length() - 3); i < musica.length(); i++) {
			sb.append(musica.charAt(i));
		}
		return sb.toString().toUpperCase();
	}

	private boolean checarExtensao() {
		String a = "MP3";
		String b = "OGG";
		return (a.contentEquals(getExtensao()) || b.contentEquals(getExtensao()));
	}

	public String getQualidade() throws UnsupportedAudioFileException,
			IOException {

		AudioFileFormat baseFileFormat = AudioSystem
				.getAudioFileFormat(musicFile);
		Map<String, Object> propriedade = ((AudioFileFormat) baseFileFormat)
				.properties();

		if (checarExtensao() == true) {
			String buffer = "";
			String key = getExtensao().toLowerCase();

			key += ".bitrate.nominal.bps";
			buffer = (String) propriedade.get(key).toString();
			int aux = Integer.parseInt(buffer) / 1000;
			buffer = Integer.toString(aux) + " Kbps | ";

			key = getExtensao().toLowerCase();
			key += ".frequency.hz";
			key = (String) propriedade.get(key).toString();
			aux = Integer.parseInt(key) / 1000;
			buffer += Integer.toString(aux) + " Khz";

			return buffer;

		} else
			return "??? Kbps | ?? Khz";
	}

	public void setMusica(String musica) {
		this.musica = musica;
		musicFile = new File(this.musica);
	}
}
