package tasks;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import utils.ClassPaths;

public class OvProjectJars extends Task {
    private String dir;
    private String projName;
    private String to;
    private boolean includeProjectJars = false;
    
    public void setDir(String dir) {
        this.dir = dir;
    }
    
    public void setProjName(String projName) {
        this.projName = projName;
    }
    
    public void setTo(String to) {
        this.to = to;
    }
    
    public void setIncludeProjectJars(String includeProjectJars) {
        this.includeProjectJars = Boolean.parseBoolean(includeProjectJars);
    }
    
    public void execute() {
        if (dir == null)
            throw new BuildException("Attribute dir was not set.");
        if (projName == null)
            throw new BuildException("Attribute projName was not set.");
        if (to == null)
            throw new BuildException("Attribute to was not set.");
        
        System.out.println(String.format("Building project jars for project %s and base dir %s", projName, dir));
        
        HashSet<String> processedProjects = new HashSet<String>();
        HashSet<String> processedJars = new HashSet<String>();
        LinkedList<String> projectsQueue = new LinkedList<>();
        projectsQueue.add(projName);
        
        while (!projectsQueue.isEmpty()) {
            String currentProject = projectsQueue.removeFirst();
            HashSet<String> currentProjectJars = ClassPaths.getProjectJars(Paths.get(dir, currentProject, ".classpath"));
            for (Iterator<String> iterator = currentProjectJars.iterator(); iterator.hasNext();) {
                String projectJar = (String) iterator.next();
                if (!processedJars.contains(projectJar)) {
                    processedJars.add(projectJar);
                }
            }
            
            HashSet<String> currentProjectRefs = ClassPaths.getProjectReferences(Paths.get(dir, currentProject, ".classpath"));
            for (Iterator<String> iterator = currentProjectRefs.iterator(); iterator.hasNext();) {
                String projectRef = (String) iterator.next();
                if (!processedProjects.contains(projectRef)) {
                    projectsQueue.add(projectRef);
                }
            }
            if (!processedProjects.contains(currentProject))
                processedProjects.add(currentProject);
        }
        
        if (includeProjectJars) {
            for (Iterator<String> iterator = processedProjects.iterator(); iterator.hasNext();) {
                String project = (String) iterator.next();
                if (project != projName) {
                    processedJars.add(String.format("/Deploy/assemblies/ov/%s.jar", project));
                }
            }
        }
        
        StringBuilder builder = new StringBuilder();
        for (Iterator<String> iterator = processedJars.iterator(); iterator.hasNext();) {
            builder.append((String) iterator.next());
            builder.append(',');
        }

        String result =  builder.toString();
        System.out.println(String.format("Project jars for project %s and base dir %s is %s", projName, dir, result));
        getProject().setNewProperty(to, result);
    }
}
