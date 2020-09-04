(function() {
    var biomeTint;

    Plugin.register('cave_plugin',
	{
        name: 'Cave Plugin',
        author: 'steve6472',
        description: 'This plugin adds CaveGame cube & face settings',
        icon: 'crop_square',
        version: '0.0.1',
        variant: 'both',
        onload()
		{
            biomeTint = new Action('toggle_biome_tint',
			{
                name: 'Biome Tint',
                description: 'Toggle Biome Tint for selected face',
                icon: 'check_box_outline_blank',
				color: 'Chartreuse',
				condition: () => Format.id === 'free',
                click: function() 
				{
                    Undo.initEdit({elements: Cube.selected});
					
					let newTint;
					let face = main_uv.face;
					
                    Cube.selected.forEach(cube =>
					{
						if (cube.faces[face].tint < 0)
							cube.faces[face].tint = 0;
						
						cube.faces[face].tint ^= 1;
						uv_dialog.forSelection('setTint', event, cube.faces[face].tint);
						
						if ((cube.faces[face].tint & 1) == 0)
						{
							main_uv.message("Biome Tint Off");
							biomeTint.setIcon(Blockbench.getIconNode('check_box_outline_blank', 'Chartreuse'));
						} else
						{
							main_uv.message("Biome Tint On");
							biomeTint.setIcon(Blockbench.getIconNode('check_box', 'Chartreuse'));
						}
                    });
					
					
                    Undo.finishEdit('Toggled parameter');
                }
            });
			
			Blockbench.on('update_selection', data => 
			{
				if (Format.id === 'free' && Cube.selected.length > 0)
				{
					let face = main_uv.face;
					biomeTint.setIcon(Blockbench.getIconNode(((Cube.selected[Cube.selected.length - 1].faces[face].tint & 1) == 0) ? 'check_box_outline_blank' : 'check_box', 'Chartreuse'));
				}
			});
			
			Toolbox.add(biomeTint);
        },
		
        onunload()
		{
            biomeTint.delete();
        }
    });

})();