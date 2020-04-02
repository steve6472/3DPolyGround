package steve6472.polyground;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 21.04.2019
 * Project: SJP
 *
 ***********************/
public class T
{
	// arguments are passed using the text field below this editor
	public static void main(String[] args)
	{
		///give @p minecraft:shulker_box{BlockEntityTag:{Items:[{Slot:0b,id:"written_book",Count:64b,tag:%s}]}}
		String start = "give @p minecraft:shulker_box{BlockEntityTag:{Items:[";
		String mid_s = "{Slot:";
		String mid_e = "b,id:\"written_book\",Count:64b,tag:";
		String end = "]}}";

		String out = start;

		for (int i = 0; i < 27; i++)
		{
			out += mid_s + i + mid_e + getBookTag() + "},";
		}
		out += end;
		System.out.println(out);
	}

	private static String getBookTag()
	{
		String start = "{pages:[\"[\\\"\\\",{\\\"text\\\":\\\"";
		String middle = "\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\"";
		String end = "\\\"}]\"],title:CustomBook,author:Player}";

		String t = "電电電買买買車楽车車紅红紅气無无無東东東馬马馬風风風乐馬风風時时時鳥鸟鳥語氣语語頭头頭魚鱼魚園园園長长長島岛島愛爱愛紙樂纸紙書书書見见見假假仮佛佛仏德德徳拜拜拝黑黑黒冰冰氷兔兔兎妒妒妬每每毎壤壤壌步步歩巢巢巣惠惠恵鞋鞋靴莓莓苺圓圆円聽听聴實实実證证証龍龙竜賣卖売龜龟亀藝艺芸戰战戦繩绳縄繪绘絵鐵铁鉄圖图図團团団轉转転廣广広惡恶悪豐丰豊腦脑脳雜杂雑壓压圧雞鸡鶏價价価";

		StringBuilder out = new StringBuilder(start);
		for (int i = 0; i < 49; i++)
		{
			out.append(getScrambled(t)).append(middle);
		}
		out.append(getScrambled(t)).append(end);
		return out.toString();
	}

	public static String getScrambled(String s)
	{
		String[] scram = s.split("");
		List<String> letters = Arrays.asList(scram);
		Collections.shuffle(letters);
		StringBuilder sb = new StringBuilder(s.length());
		for (String c : letters)
		{
			sb.append(c);
		}
		return sb.toString();
	}
}
