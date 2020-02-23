package com.nuparu.sevendaystomine.util.computer;

import java.util.HashMap;
import java.util.Objects;
import java.util.Map.Entry;

import com.nuparu.sevendaystomine.SevenDaysToMine;

import net.minecraft.util.ResourceLocation;

public class ApplicationRegistry {
    
	public static final ApplicationRegistry INSTANCE = new ApplicationRegistry();
	
	private HashMap<ResourceLocation, Application> registry = new HashMap<ResourceLocation, Application>();
	
	
	public void registerApp(Application app, String name) {
		registerApp(app, new ResourceLocation(SevenDaysToMine.MODID,name));
	}
	
	public void registerApp(Application app, ResourceLocation res) {
		app.key = res;
		registry.put(res, app);
	}
	
	public Application getByRes(ResourceLocation res) {
		Application app = registry.get(res);
		return app;
	}
	
	public Application getByString(String name) {
		return getByRes(new ResourceLocation(SevenDaysToMine.MODID,name));
	}
	
	public ResourceLocation getResByApp(Application app) {
		for (Entry<ResourceLocation, Application> entry : registry.entrySet()) {
	        if (Objects.equals(app, entry.getValue())) {
	            return entry.getKey();
	        }
	    }
		return null;
	}
	
	public void register() {
		registerApp(new Application(new ResourceLocation(SevenDaysToMine.MODID,"textures/apps/shell.png"),"shell"),"shell");
		registerApp(new Application(new ResourceLocation(SevenDaysToMine.MODID,"textures/apps/notes.png"),"notes"),"notes");
	}
}
