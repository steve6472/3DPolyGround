package steve6472.polyground.generator.models;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 02.07.2020
 * Project: CaveGame
 *
 ***********************/
public class CondTest implements IModel
{
	@Override
	public String build()
	{
		return  """
			{"cubes": [{
			    "from": [
			        0,
			        0,
			        0
			    ],
			    "to": [
			        16,
			        16,
			        16
			    ],
			    "faces": {
			        "east": {
			            "texture": "smooth_stone",
			            "tint": [
			                200,
			                200,
			                200
			            ]
			        },
			        "south": {
			            "texture": "smooth_stone",
			            "tint": [
			                200,
			                200,
			                200
			            ]
			        },
			        "north": {
			            "texture": "smooth_stone",
			            "tint": [
			                200,
			                200,
			                200
			            ]
			        },
			        "west": {
			            "texture": "smooth_stone",
			            "tint": [
			                200,
			                200,
			                200
			            ]
			        },
			        "up": {"conditionedTexture": [
			            {
			                "condition": "(([0, 1, 0] != #transparent) && (([0, 1, 0] == #slab_bottom) || ([0, 1, 0] == #full)))",
			                "isVisible": false
			            },
			            {
			                "texture": "smooth_stone/cross",
			                "andChain": "[1, 0, 0] == cond_test && [-1, 0, 0] == cond_test && [0, 0, 1] == cond_test && [0, 0, -1] == cond_test && [1, 0, 1] != cond_test && [-1, 0, 1] != cond_test && [-1, 0, -1] != cond_test && [1, 0, -1] != cond_test"
			            },
			            {
			                "texture": "smooth_stone/corner_3",
			                "andChain": "[1, 0, 0] == cond_test && [-1, 0, 0] == cond_test && [0, 0, 1] == cond_test && [0, 0, -1] == cond_test && [1, 0, 1] != cond_test && [-1, 0, 1] != cond_test && [-1, 0, -1] == cond_test && [1, 0, -1] != cond_test"
			            },
			            {
			                "texture": "smooth_stone/corner_3",
			                "rotation": "R_270",
			                "andChain": "[1, 0, 0] == cond_test && [-1, 0, 0] == cond_test && [0, 0, 1] == cond_test && [0, 0, -1] == cond_test && [1, 0, 1] != cond_test && [-1, 0, 1] != cond_test && [-1, 0, -1] != cond_test && [1, 0, -1] == cond_test"
			            },
			            {
			                "texture": "smooth_stone/corner_3",
			                "rotation": "R_90",
			                "andChain": "[1, 0, 0] == cond_test && [-1, 0, 0] == cond_test && [0, 0, 1] == cond_test && [0, 0, -1] == cond_test && [1, 0, 1] != cond_test && [-1, 0, 1] == cond_test && [-1, 0, -1] != cond_test && [1, 0, -1] != cond_test"
			            },
			            {
			                "texture": "smooth_stone/corner_3",
			                "rotation": "R_180",
			                "andChain": "[1, 0, 0] == cond_test && [-1, 0, 0] == cond_test && [0, 0, 1] == cond_test && [0, 0, -1] == cond_test && [1, 0, 1] == cond_test && [-1, 0, 1] != cond_test && [-1, 0, -1] != cond_test && [1, 0, -1] != cond_test"
			            },
			            {
			                "texture": "smooth_stone/corner_2",
			                "andChain": "[1, 0, 0] == cond_test && [-1, 0, 0] == cond_test && [0, 0, 1] == cond_test && [0, 0, -1] == cond_test && [1, 0, 1] != cond_test && [-1, 0, 1] == cond_test && [-1, 0, -1] == cond_test && [1, 0, -1] != cond_test"
			            },
			            {
			                "texture": "smooth_stone/corner_2",
			                "rotation": "R_180",
			                "andChain": "[1, 0, 0] == cond_test && [-1, 0, 0] == cond_test && [0, 0, 1] == cond_test && [0, 0, -1] == cond_test && [1, 0, 1] == cond_test && [-1, 0, 1] != cond_test && [-1, 0, -1] != cond_test && [1, 0, -1] == cond_test"
			            },
			            {
			                "texture": "smooth_stone/corner_2",
			                "rotation": "R_270",
			                "andChain": "[1, 0, 0] == cond_test && [-1, 0, 0] == cond_test && [0, 0, 1] == cond_test && [0, 0, -1] == cond_test && [1, 0, 1] != cond_test && [-1, 0, 1] != cond_test && [-1, 0, -1] == cond_test && [1, 0, -1] == cond_test"
			            },
			            {
			                "texture": "smooth_stone/corner_2",
			                "rotation": "R_90",
			                "andChain": "[1, 0, 0] == cond_test && [-1, 0, 0] == cond_test && [0, 0, 1] == cond_test && [0, 0, -1] == cond_test && [1, 0, 1] == cond_test && [-1, 0, 1] == cond_test && [-1, 0, -1] != cond_test && [1, 0, -1] != cond_test"
			            },
			            {
			                "texture": "smooth_stone/corner_2_diagonal",
			                "andChain": "[1, 0, -1] != cond_test && [1, 0, 0] == cond_test && [1, 0, 1] == cond_test && [0, 0, -1] == cond_test && [0, 0, 1] == cond_test && [-1, 0, -1] == cond_test && [-1, 0, 0] == cond_test && [-1, 0, 1] != cond_test"
			            },
			            {
			                "texture": "smooth_stone/corner_2_diagonal",
			                "rotation": "R_90",
			                "andChain": "[1, 0, -1] == cond_test && [1, 0, 0] == cond_test && [1, 0, 1] != cond_test && [0, 0, -1] == cond_test && [0, 0, 1] == cond_test && [-1, 0, -1] != cond_test && [-1, 0, 0] == cond_test && [-1, 0, 1] == cond_test"
			            },
			            {
			                "texture": "smooth_stone/corner",
			                "andChain": "[1, 0, -1] == cond_test && [1, 0, 0] == cond_test && [1, 0, 1] != cond_test && [0, 0, -1] == cond_test && [0, 0, 1] == cond_test && [-1, 0, -1] == cond_test && [-1, 0, 0] == cond_test && [-1, 0, 1] == cond_test"
			            },
			            {
			                "texture": "smooth_stone/corner",
			                "rotation": "R_90",
			                "andChain": "[1, 0, -1] != cond_test && [1, 0, 0] == cond_test && [1, 0, 1] == cond_test && [0, 0, -1] == cond_test && [0, 0, 1] == cond_test && [-1, 0, -1] == cond_test && [-1, 0, 0] == cond_test && [-1, 0, 1] == cond_test"
			            },
			            {
			                "texture": "smooth_stone/corner",
			                "rotation": "R_180",
			                "andChain": "[1, 0, -1] == cond_test && [1, 0, 0] == cond_test && [1, 0, 1] == cond_test && [0, 0, -1] == cond_test && [0, 0, 1] == cond_test && [-1, 0, -1] != cond_test && [-1, 0, 0] == cond_test && [-1, 0, 1] == cond_test"
			            },
			            {
			                "texture": "smooth_stone/corner",
			                "rotation": "R_270",
			                "andChain": "[1, 0, -1] == cond_test && [1, 0, 0] == cond_test && [1, 0, 1] == cond_test && [0, 0, -1] == cond_test && [0, 0, 1] == cond_test && [-1, 0, -1] == cond_test && [-1, 0, 0] == cond_test && [-1, 0, 1] != cond_test"
			            },
			            {
			                "texture": "smooth_stone/side_corner",
			                "andChain": "[1, 0, 0] != cond_test && [0, 0, -1] == cond_test && [0, 0, 1] != cond_test && [-1, 0, -1] == cond_test && [-1, 0, 0] == cond_test"
			            },
			            {
			                "texture": "smooth_stone/side_corner",
			                "rotation": "R_90",
			                "andChain": "[1, 0, 0] != cond_test && [0, 0, -1] != cond_test && [0, 0, 1] == cond_test && [-1, 0, 0] == cond_test && [-1, 0, 1] == cond_test"
			            },
			            {
			                "texture": "smooth_stone/side_corner",
			                "rotation": "R_180",
			                "andChain": "[1, 0, 0] == cond_test && [1, 0, 1] == cond_test && [0, 0, -1] != cond_test && [0, 0, 1] == cond_test && [-1, 0, 0] != cond_test"
			            },
			            {
			                "texture": "smooth_stone/side_corner",
			                "rotation": "R_270",
			                "andChain": "[1, 0, -1] == cond_test && [1, 0, 0] == cond_test && [0, 0, -1] == cond_test && [0, 0, 1] != cond_test && [-1, 0, 0] != cond_test"
			            },
			            {
			                "texture": "smooth_stone/side",
			                "andChain": "[1, 0, 0] != cond_test && [0, 0, -1] == cond_test && [0, 0, 1] == cond_test && [-1, 0, -1] == cond_test && [-1, 0, 0] == cond_test && [-1, 0, 1] == cond_test"
			            },
			            {
			                "texture": "smooth_stone/side",
			                "rotation": "R_90",
			                "andChain": "[1, 0, 0] == cond_test && [1, 0, 1] == cond_test && [0, 0, -1] != cond_test && [0, 0, 1] == cond_test && [-1, 0, 0] == cond_test && [-1, 0, 1] == cond_test"
			            },
			            {
			                "texture": "smooth_stone/side",
			                "rotation": "R_180",
			                "andChain": "[1, 0, -1] == cond_test && [1, 0, 0] == cond_test && [1, 0, 1] == cond_test && [0, 0, -1] == cond_test && [0, 0, 1] == cond_test && [-1, 0, 0] != cond_test"
			            },
			            {
			                "texture": "smooth_stone/side",
			                "rotation": "R_270",
			                "andChain": "[1, 0, -1] == cond_test && [1, 0, 0] == cond_test && [0, 0, -1] == cond_test && [0, 0, 1] != cond_test && [-1, 0, -1] == cond_test && [-1, 0, 0] == cond_test"
			            },
			            {
			                "texture": "smooth_stone/u",
			                "andChain": "[1, 0, 0] != cond_test && [0, 0, -1] != cond_test && [0, 0, 1] != cond_test && [-1, 0, 0] == cond_test"
			            },
			            {
			                "texture": "smooth_stone/u",
			                "rotation": "R_90",
			                "andChain": "[1, 0, 0] != cond_test && [0, 0, -1] != cond_test && [0, 0, 1] == cond_test && [-1, 0, 0] != cond_test"
			            },
			            {
			                "texture": "smooth_stone/u",
			                "rotation": "R_180",
			                "andChain": "[1, 0, 0] == cond_test && [0, 0, -1] != cond_test && [0, 0, 1] != cond_test && [-1, 0, 0] != cond_test"
			            },
			            {
			                "texture": "smooth_stone/u",
			                "rotation": "R_270",
			                "andChain": "[1, 0, 0] != cond_test && [0, 0, -1] == cond_test && [0, 0, 1] != cond_test && [-1, 0, 0] != cond_test"
			            },
			            {
			                "texture": "smooth_stone/none",
			                "andChain": "[1, 0, 0] == cond_test && [-1, 0, 0] == cond_test && [0, 0, 1] == cond_test && [0, 0, -1] == cond_test && [1, 0, 1] == cond_test && [-1, 0, 1] == cond_test && [-1, 0, -1] == cond_test && [1, 0, -1] == cond_test"
			            },
			            {
			                "texture": "smooth_stone/sides",
			                "andChain": "[1, 0, 0] == cond_test && [0, 0, -1] != cond_test && [0, 0, 1] != cond_test && [-1, 0, 0] == cond_test"
			            },
			            {
			                "texture": "smooth_stone/sides",
			                "rotation": "R_90",
			                "andChain": "[1, 0, 0] != cond_test && [0, 0, -1] == cond_test && [0, 0, 1] == cond_test && [-1, 0, 0] != cond_test"
			            },
			            {
			                "texture": "smooth_stone/side_corner_corner",
			                "andChain": "[1, 0, 0] != cond_test && [0, 0, -1] == cond_test && [0, 0, 1] != cond_test && [-1, 0, 0] == cond_test"
			            },
			            {
			                "texture": "smooth_stone/side_corner_corner",
			                "rotation": "R_90",
			                "andChain": "[1, 0, 0] != cond_test && [0, 0, -1] != cond_test && [0, 0, 1] == cond_test && [-1, 0, 0] == cond_test"
			            },
			            {
			                "texture": "smooth_stone/side_corner_corner",
			                "rotation": "R_180",
			                "andChain": "[1, 0, 0] == cond_test && [0, 0, -1] != cond_test && [0, 0, 1] == cond_test && [-1, 0, 0] != cond_test"
			            },
			            {
			                "texture": "smooth_stone/side_corner_corner",
			                "rotation": "R_270",
			                "andChain": "[1, 0, 0] == cond_test && [0, 0, -1] == cond_test && [0, 0, 1] != cond_test && [-1, 0, 0] != cond_test"
			            },
			            {
			                "texture": "smooth_stone/side_corner_2",
			                "andChain": "[1, 0, 0] != cond_test && [0, 0, -1] == cond_test && [0, 0, 1] == cond_test && [-1, 0, -1] != cond_test && [-1, 0, 0] == cond_test && [-1, 0, 1] != cond_test"
			            },
			            {
			                "texture": "smooth_stone/side_corner_2",
			                "rotation": "R_90",
			                "andChain": "[1, 0, 0] == cond_test && [1, 0, 1] != cond_test && [0, 0, -1] != cond_test && [0, 0, 1] == cond_test && [-1, 0, 0] == cond_test && [-1, 0, 1] != cond_test"
			            },
			            {
			                "texture": "smooth_stone/side_corner_2",
			                "rotation": "R_180",
			                "andChain": "[1, 0, -1] != cond_test && [1, 0, 0] == cond_test && [1, 0, 1] != cond_test && [0, 0, -1] == cond_test && [0, 0, 1] == cond_test && [-1, 0, 0] != cond_test"
			            },
			            {
			                "texture": "smooth_stone/side_corner_2",
			                "rotation": "R_270",
			                "andChain": "[1, 0, -1] != cond_test && [1, 0, 0] == cond_test && [0, 0, -1] == cond_test && [0, 0, 1] != cond_test && [-1, 0, -1] != cond_test && [-1, 0, 0] == cond_test"
			            },
			            {
			                "texture": "smooth_stone/side_corner_1_r",
			                "andChain": "[1, 0, 0] != cond_test && [0, 0, -1] == cond_test && [0, 0, 1] == cond_test && [-1, 0, -1] == cond_test && [-1, 0, 0] == cond_test && [-1, 0, 1] != cond_test"
			            },
			            {
			                "texture": "smooth_stone/side_corner_1_r",
			                "rotation": "R_90",
			                "andChain": "[1, 0, 0] == cond_test && [1, 0, 1] != cond_test && [0, 0, -1] != cond_test && [0, 0, 1] == cond_test && [-1, 0, 0] == cond_test && [-1, 0, 1] == cond_test"
			            },
			            {
			                "texture": "smooth_stone/side_corner_1_r",
			                "rotation": "R_180",
			                "andChain": "[1, 0, -1] != cond_test && [1, 0, 0] == cond_test && [1, 0, 1] == cond_test && [0, 0, -1] == cond_test && [0, 0, 1] == cond_test && [-1, 0, 0] != cond_test"
			            },
			            {
			                "texture": "smooth_stone/side_corner_1_r",
			                "rotation": "R_270",
			                "andChain": "[1, 0, -1] == cond_test && [1, 0, 0] == cond_test && [0, 0, -1] == cond_test && [0, 0, 1] != cond_test && [-1, 0, -1] != cond_test && [-1, 0, 0] == cond_test"
			            },
			            {
			                "texture": "smooth_stone/side_corner_1_l",
			                "andChain": "[1, 0, 0] != cond_test && [0, 0, -1] == cond_test && [0, 0, 1] == cond_test && [-1, 0, -1] != cond_test && [-1, 0, 0] == cond_test && [-1, 0, 1] == cond_test"
			            },
			            {
			                "texture": "smooth_stone/side_corner_1_l",
			                "rotation": "R_90",
			                "andChain": "[1, 0, 0] == cond_test && [1, 0, 1] == cond_test && [0, 0, -1] != cond_test && [0, 0, 1] == cond_test && [-1, 0, 0] == cond_test && [-1, 0, 1] != cond_test"
			            },
			            {
			                "texture": "smooth_stone/side_corner_1_l",
			                "rotation": "R_180",
			                "andChain": "[1, 0, -1] == cond_test && [1, 0, 0] == cond_test && [1, 0, 1] != cond_test && [0, 0, -1] == cond_test && [0, 0, 1] == cond_test && [-1, 0, 0] != cond_test"
			            },
			            {
			                "texture": "smooth_stone/side_corner_1_l",
			                "rotation": "R_270",
			                "andChain": "[1, 0, -1] != cond_test && [1, 0, 0] == cond_test && [0, 0, -1] == cond_test && [0, 0, 1] != cond_test && [-1, 0, -1] == cond_test && [-1, 0, 0] == cond_test"
			            },
			            {
			                "texture": "smooth_stone/all",
			                "isVisible": true
			            }
			        ]},
			        "down": {
			            "texture": "smooth_stone",
			            "tint": [
			                200,
			                200,
			                200
			            ]
			        }
			    }
			}]}
			""";
	}
}
