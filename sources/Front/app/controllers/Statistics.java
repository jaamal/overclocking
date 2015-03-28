package controllers;

import models.statistics.StatisticType;
import play.Logger;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.results.BadRequest;
import services.statistics.IPlotParameters;
import services.statistics.IPlotParametersFactory;
import services.statistics.IPlotService;
import configuration.Configurator;

public class Statistics extends Controller {
	
	private static IPlotService plotService;
	private static IPlotParametersFactory plotParametersFactory;
	
    public static void run() {
        StatisticType[] allTypes = StatisticType.values();
        render("Statistics/statistics.html", allTypes);
    }
    
    @Before
    static void init() {
    	plotService = Configurator.getContainer().get(IPlotService.class);
    	plotParametersFactory = Configurator.getContainer().get(IPlotParametersFactory.class);
    }
    
    public static void build() {
    	try {
	        StatisticType xType = StatisticType.valueOf(request.params.get("xType"));
	        StatisticType yType = StatisticType.valueOf(request.params.get("yType"));
	        
	        IPlotParameters plotParams = plotParametersFactory.create(xType, yType);
	        renderJSON(plotService.get(plotParams));
    	}
    	catch(Exception e) {
    		Logger.error("Fail to build plot.", e);
    		throw new BadRequest();
    	}
    }
}
