package steve6472.polyground.audio;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.openal.*;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC11.*;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.libc.LibCStdlib.free;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 25.10.2020
 * Project: CaveGame
 *
 ***********************/
public class SoundMaster
{
	private static final Vector3f UP = new Vector3f(0, 1, 0);
	private static final Vector3f AT = new Vector3f(0, 0, 0);
	private static final Matrix4f INV_CAM = new Matrix4f();
	private static final float[] ORIENTATION_DATA = new float[6];

	private static final List<Integer> buffers = new ArrayList<>();

	private static long device, context;

	public static void setup()
	{
		String defaultDeviceName = alcGetString(0, ALC10.ALC_DEFAULT_DEVICE_SPECIFIER);
		device = alcOpenDevice(defaultDeviceName);

		int[] attributes = {0};
		context = alcCreateContext(device, attributes);
		alcMakeContextCurrent(context);

		ALCCapabilities alcCapabilities = ALC.createCapabilities(device);
		ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

		if (!alCapabilities.OpenAL11)
		{
			throw new RuntimeException("OpenAL 11 is not supported");
		}
	}

	public static void setListenerPosition(float x, float y, float z)
	{
		AL11.alListener3f(AL_POSITION, x, y, z);
	}

	public static void setListenerOrientation(Matrix4f viewMatrix)
	{
		AT.set(0, 0, -1);
		UP.set(0, 1, 0);

		INV_CAM.set(viewMatrix).invert();
		INV_CAM.transformDirection(AT);
		INV_CAM.transformDirection(UP);

		ORIENTATION_DATA[0] = AT.x();
		ORIENTATION_DATA[1] = AT.y();
		ORIENTATION_DATA[2] = AT.z();
		ORIENTATION_DATA[3] = UP.x();
		ORIENTATION_DATA[4] = UP.y();
		ORIENTATION_DATA[5] = UP.z();

		AL11.alListenerfv(AL_ORIENTATION, ORIENTATION_DATA);
	}

	public static int loadSound(String path)
	{
		stackPush();
		IntBuffer channelsBuffer = stackMallocInt(1);
		stackPush();
		IntBuffer sampleRateBuffer = stackMallocInt(1);

		ShortBuffer rawAudioBuffer = stb_vorbis_decode_filename(path, channelsBuffer, sampleRateBuffer);

		int channels = channelsBuffer.get();
		int sampleRate = sampleRateBuffer.get();

		stackPop();
		stackPop();



		//Find the correct OpenAL format
		int format = -1;
		if(channels == 1) {
			format = AL_FORMAT_MONO16;
		} else if(channels == 2) {
			format = AL_FORMAT_STEREO16;
		}

		//Request space for the buffer
		int bufferPointer = alGenBuffers();

		//Send the data to OpenAL
		alBufferData(bufferPointer, format, rawAudioBuffer, sampleRate);

		//Free the memory allocated by STB
		free(rawAudioBuffer);

		buffers.add(bufferPointer);

		return bufferPointer;
	}

	public static void destroy()
	{
		for (int buffer : buffers)
		{
			AL11.alDeleteBuffers(buffer);
		}

		alcDestroyContext(context);
		alcCloseDevice(device);
	}
}
