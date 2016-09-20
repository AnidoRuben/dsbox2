package es.uvigo.esei.dsbox.gui.controllers;

import es.uvigo.esei.dsbox.core.config.DSBOXConfig;
import es.uvigo.esei.dsbox.core.manager.SimulationEngine;
import es.uvigo.esei.dsbox.core.manager.SingleInstanceSimulationEngine;
import es.uvigo.esei.dsbox.core.manager.VMDriverException;
import es.uvigo.esei.dsbox.core.model.HostType;
import es.uvigo.esei.dsbox.core.model.SimulationSpec;
import es.uvigo.esei.dsbox.core.model.exceptions.DSBOXException;
import es.uvigo.esei.dsbox.core.model.execution.ExecutionSpec;
import es.uvigo.esei.dsbox.core.xml.DSBOXXMLDAO;
import es.uvigo.esei.dsbox.gui.views.MainWindow;
import es.uvigo.esei.dsbox.virtualbox.execution.VirtualBoxDriverSpec;
import es.uvigo.esei.dsbox.virtualbox.manager.VirtualBoxDriver;
import java.io.File;
import java.util.List;

public class MainController {
    
    private String workingDirectory;
    private MainWindow mainWindow;
    private DSBOXConfig dsboxConfig;
    private DSBOXXMLDAO dao;
    private SimulationEngine engine;
    private VirtualBoxDriverSpec vboxDriverSpec;
    private VirtualBoxDriver vboxDriver;
    private List<HostType> registeredHostTypes;
    
    private boolean simulationIsRunning = false;
    private String runningSimulationName = null;
    private ExecutionSpec executionSpec = null;
    
    public MainController() {
    }
    
    public MainController(String workingDirectory) {
        this.workingDirectory = workingDirectory;
        this.dao = new DSBOXXMLDAO();
        this.dsboxConfig = new DSBOXConfig(workingDirectory, dao);
    }
    
    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }
    
    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
        this.dsboxConfig = new DSBOXConfig(workingDirectory, dao);
    }
    
    public String getWorkingDirectory() {
        return workingDirectory;
    }
    
    public void startUp() throws DSBOXException, VMDriverException {
        this.registeredHostTypes = dsboxConfig.getRegisteredHostTypes();

        // Create/load VBox driver Spec
        File vboxDriverSpecFile = new File(dsboxConfig.getConfigDir(), VirtualBoxDriverSpec.VBOXDRIVER_CONFIG_FILE);
        if (!vboxDriverSpecFile.exists()) {
            vboxDriverSpec = VirtualBoxDriverSpec.loadFromFile(vboxDriverSpecFile.getAbsolutePath());
        }
        else {
            vboxDriverSpec = VirtualBoxDriverSpec.createDefaultSpec(dsboxConfig);
            VirtualBoxDriverSpec.saveToFile(vboxDriverSpec, vboxDriverSpecFile.getAbsolutePath());
        }

        vboxDriver = new VirtualBoxDriver();
        vboxDriver.setVMDriverSpec(vboxDriverSpec);
        
        dao.addKnownJAXBClasses(vboxDriver.exposeJAXBClasses());
        
        engine = new SingleInstanceSimulationEngine();
        engine.addVMDriver(vboxDriver);
        engine.initialize();
        
        /* TODO: en simulacion
        for (HostType hostType : registeredHostTypes) {
            if (!engine.isHostTypeRegistered(hostType)) {
                engine.registerHostType(hostType);
            }
        }
        */
    }
    
    public void shutDown() throws VMDriverException {
        if (simulationIsRunning && (executionSpec != null)) {
            engine.stopSimulation(runningSimulationName, executionSpec);
        }
        engine.finalize();
    }
    
    public boolean isValidConfiguration() {
        return dsboxConfig.checkDSBOXConfiguration();
    }
    
    public void createWorkingDirectory() throws DSBOXException {
        dsboxConfig.initializeDSBOXConfig();
    }
    
    public List<HostType> getRegisteredHostTypes() {
        return this.dsboxConfig.getRegisteredHostTypes();
    }
    
    public void registerNewHostType(HostType newHostType) throws DSBOXException, VMDriverException {
        dsboxConfig.addHostType(newHostType);
        if (!engine.isHostTypeRegistered(newHostType)) {
            engine.registerHostType(newHostType);
        }
    }
    
    public void unRegisterHostType(HostType hostType) throws DSBOXException, VMDriverException {
        dsboxConfig.removeHostType(hostType);
        engine.unregisterHostType(hostType);
    }
    
    public void saveSimulationSpec(SimulationSpec simulationSpec, String simulationSpecFilename) throws DSBOXException {
        // Set simulation dir
        // TODO better update on first start 
        File simulationDir = new File(dsboxConfig.getSimulationsDir(), simulationSpec.getName().replace(" ", "_"));
        simulationSpec.setSimulationDir(simulationDir.getAbsolutePath());
        
        dao.saveSimulationSpecToFile(simulationSpecFilename, simulationSpec);
    }
    
    public SimulationSpec loadSimulationSpec(String simulationSpecFilename) throws DSBOXException {
        return dao.loadSimulationSpecFromFile(simulationSpecFilename);
    }
    
    public void startSimulation(SimulationSpec simulationSpec) throws VMDriverException {
        String simulationName = simulationSpec.getName();
        
        // Check host type are dowloaded and registered in VMDriver
        // Control download -> cancel download => abort startSimulation
        
        executionSpec = engine.initSimulation(simulationName, simulationSpec.getNetworkSpec());
        
        engine.startSimulation(simulationName, executionSpec);
        runningSimulationName = simulationName;
        simulationIsRunning = true;
    }
    
    public void pauseSimulation(SimulationSpec simulationSpec) throws VMDriverException {
        if (simulationIsRunning && (executionSpec != null)) {
            executionSpec = engine.pauseSimulation(runningSimulationName, executionSpec);
        }
    }
    
    public void resumeSimulation(SimulationSpec simulationSpec) throws VMDriverException {
        if (simulationIsRunning && (executionSpec != null)) {
            executionSpec = engine.resumeSimulation(runningSimulationName, executionSpec);
        }
    }
    
    public void stopSimulation(SimulationSpec simulationSpec) throws VMDriverException {
        if (simulationIsRunning && (executionSpec != null)) {
            executionSpec = engine.stopSimulation(runningSimulationName, executionSpec);
            simulationIsRunning = false;
            runningSimulationName = "";
            executionSpec = null;
        }
    }
    
    public String getImagesDirectory() {
        return dsboxConfig.getImagesDir().getAbsolutePath();
    }
    
    public String getSimulationsDirectory() {
        return dsboxConfig.getSimulationsDir().getAbsolutePath();
    }
    
    public VirtualBoxDriverSpec getVBoxDriverSpec() {
        return this.vboxDriverSpec;
    }
    
}
