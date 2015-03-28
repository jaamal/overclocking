package controllers;

import java.net.InetAddress;
import java.util.Arrays;

import models.Admin.AppStatsModel;
import models.Admin.CPUStatsModel;
import models.Admin.ProcessInfoDescendingComparator;
import models.Admin.ProcessModel;
import models.Admin.ProcessStatsModel;
import models.Admin.RAMStatsModel;
import play.Logger;
import play.mvc.Before;
import play.mvc.Controller;

import com.jezhumble.javasysmon.JavaSysMon;
import com.jezhumble.javasysmon.MemoryStats;
import com.jezhumble.javasysmon.ProcessInfo;

import configuration.Configurator;
import factories.IMeasurableValuesFactory;

public class Admin extends Controller {
	private final static int processBound = 15;
	
	private static JavaSysMon sysMon;
	private static IMeasurableValuesFactory measurableValuesFactory;

	@Before
    static void init() {
		sysMon = new JavaSysMon();
		measurableValuesFactory = Configurator.getContainer().get(IMeasurableValuesFactory.class);
    }
	
	public static void run() {
		try {
			AppStatsModel appStats = new AppStatsModel();
			appStats.machineName = InetAddress.getLocalHost().getHostName();
			appStats.osName = sysMon.osName();
			appStats.cpuStats = buildCPUStats();
			appStats.ramStats = buildRAMStats();
			appStats.processStats = buildProcessStats();
			
			render("Admin/appStats.html", appStats);
		}
		catch(Exception e) {
			Logger.error(e, "Fail to gather information from sysMon. Message: " + e.getMessage());
            badRequest();
		}
    }
	
	private static CPUStatsModel buildCPUStats() {
		CPUStatsModel result = new CPUStatsModel();
		result.frequency = measurableValuesFactory.createMHz(sysMon.cpuFrequencyInHz());
		result.number = measurableValuesFactory.createNumeric(sysMon.numCpus());
		return result;
	}
	
	public static RAMStatsModel buildRAMStats() {
		RAMStatsModel result = new RAMStatsModel();
		MemoryStats memorySnapshot = sysMon.physical();
		result.freeSpace = measurableValuesFactory.createMb(memorySnapshot.getFreeBytes());
		result.totalSpace = measurableValuesFactory.createMb(memorySnapshot.getTotalBytes());
		float unusedRAM = ((float) memorySnapshot.getFreeBytes() / memorySnapshot.getTotalBytes()) * 100;
		result.freeSpacePercent = measurableValuesFactory.createPercent(unusedRAM);
		return result;
	}
	
	private static ProcessStatsModel buildProcessStats() {
		ProcessStatsModel result = new ProcessStatsModel();
		ProcessInfo[] processTable = sysMon.processTable();
		result.total = processTable.length;
		
		Arrays.sort(processTable, new ProcessInfoDescendingComparator());	
		ProcessInfo[] topProcessInfos = Arrays.copyOf(processTable, processBound);
		ProcessModel[] processModels = new ProcessModel[processBound];
		for(int i = 0; i < processBound; i++) {
			ProcessModel processModel = new ProcessModel();
			processModel.pId = topProcessInfos[i].getPid();
			processModel.name = topProcessInfos[i].getName();
			processModel.ramUsage = measurableValuesFactory.createMb(topProcessInfos[i].getTotalBytes());
			processModel.command = topProcessInfos[i].getCommand();
			processModels[i] = processModel;
		}
		result.processes = processModels;
		return result;
	}

}
