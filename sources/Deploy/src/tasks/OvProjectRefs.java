package tasks;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import utils.ClassPaths;

public class OvProjectRefs extends Task {
    private String dir;
    private String projName;
    private String to;
    
    public void setDir(String dir) {
        this.dir = dir;
    }
    
    public void setProjName(String projName) {
        this.projName = projName;
    }
    
    public void setTo(String to) {
        this.to = to;
    }
    
    public void execute() {
        if (dir == null)
            throw new BuildException("Attribute dir was not set.");
        if (projName == null)
            throw new BuildException("Attribute projName was not set.");
        if (to == null)
            throw new BuildException("Attribute to was not set.");
        
        System.out.println(String.format("Building project references for project %s and base dir %s", projName, dir));
        
        HashSet<String> processedProjects = new HashSet<String>();
        LinkedList<String> projectsQueue = new LinkedList<>();
        projectsQueue.add(projName);
        
        while (!projectsQueue.isEmpty()) {
            String currentProject = projectsQueue.removeFirst();
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
        
        StringBuilder builder = new StringBuilder();
        for (Iterator<String> iterator = processedProjects.iterator(); iterator.hasNext();) {
            builder.append((String) iterator.next());
            builder.append(',');
        }

        String result =  builder.toString();
        System.out.println(String.format("Project references for project %s and base dir %s is %s", projName, dir, result));
        getProject().setNewProperty(to, result);
    }
}
