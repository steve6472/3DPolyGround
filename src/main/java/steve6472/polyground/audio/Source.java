package steve6472.polyground.audio;

import steve6472.polyground.CaveGame;

import static org.lwjgl.openal.AL11.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 25.10.2020
 * Project: CaveGame
 *
 ***********************/
public class Source
{
	private int sourceId;

	public Source()
	{
		sourceId = alGenSources();
		alSourcef(sourceId, AL_GAIN, 1);
		alSourcef(sourceId, AL_PITCH, 1);
		alSource3f(sourceId, AL_POSITION, 0, 0, 0);
	}

	public void play(int buffer)
	{
		stop();
		alSourcei(sourceId, AL_BUFFER, buffer);
		unpause();
	}

	public void pause()
	{
		alSourcePause(sourceId);
	}

	public void unpause()
	{
		alSourcePlay(sourceId);
	}

	public void stop()
	{
		alSourceStop(sourceId);
	}

	public boolean isPlaying()
	{
		return alGetSourcei(sourceId, AL_SOURCE_STATE) == AL_PLAYING;
	}

	public void setVelocity(float x, float y, float z)
	{
		alSource3f(sourceId, AL_VELOCITY, x, y, z);
	}

	public void setLooping(boolean loop)
	{
		alSourcei(sourceId, AL_LOOPING, loop ? AL_TRUE : AL_FALSE);
	}

	public void setVolume(float volume)
	{
		alSourcef(sourceId, AL_GAIN, volume * CaveGame.getInstance().options.masterVolume);
	}

	public void setPitch(float pitch)
	{
		alSourcef(sourceId, AL_PITCH, pitch);
	}

	public void setPosition(float x, float y, float z)
	{
		alSource3f(sourceId, AL_POSITION, x, y, z);
	}

	public void delete()
	{
		stop();
		alDeleteSources(sourceId);
	}
}
