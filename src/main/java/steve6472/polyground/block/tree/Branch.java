package steve6472.polyground.block.tree;

import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 17.10.2020
 * Project: CaveGame
 *
 ***********************/
public record Branch(Tree.Node start, List<Tree.Node> nodes, int maxSize, int maxCount)
{
}
