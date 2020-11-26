package steve6472.polyground.block.blockdata.logic;

import steve6472.polyground.block.blockdata.logic.gates.AndGate;
import steve6472.polyground.block.blockdata.logic.gates.NotGate;
import steve6472.polyground.block.blockdata.logic.other.LogOne;
import steve6472.polyground.block.blockdata.logic.other.LogZero;
import steve6472.polyground.block.blockdata.logic.other.Light;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.11.2020
 * Project: CaveGame
 *
 ***********************/
public class LogicTest
{
	public static void main(String[] args)
	{
		LogOne one = new LogOne();
		LogZero zero = new LogZero();
		Light outputGate = new Light();

		AndGate and = new AndGate();
		NotGate not = new NotGate();

//		connect(one, and, 0, 0);
//		connect(zero, not, 0, 0);
//		connect(not, and, 0, 1);
//		connect(and, outputGate, 0, 0);

//		one.updateOutputs();
//		System.out.println("-".repeat(16));
//		zero.updateOutputs();
	}

	private static void connect(AbstractGate from, AbstractGate to, int fromOutput, int toInput)
	{
		from.connectOutput(fromOutput, to, toInput);
		to.connectInput(toInput, from, fromOutput);
	}
}
