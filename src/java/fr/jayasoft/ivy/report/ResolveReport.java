/*
 * This file is subject to the license found in LICENCE.TXT in the root directory of the project.
 * 
 * #SNAPSHOT#
 */
package fr.jayasoft.ivy.report;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import fr.jayasoft.ivy.IvyNode;
import fr.jayasoft.ivy.ModuleDescriptor;

/**
 * Represents a whole resolution report for a module
 */
public class ResolveReport {
    private ModuleDescriptor _md;
    private Map _confReports = new LinkedHashMap();
    public ResolveReport(ModuleDescriptor md) {
        _md = md;
    }
    public void addReport(String conf, ConfigurationResolveReport report) {
        _confReports.put(conf, report);
    }
    public ConfigurationResolveReport getConfigurationReport(String conf) {
        return (ConfigurationResolveReport)_confReports.get(conf);
    }
    public String[] getConfigurations() {
        return (String[])_confReports.keySet().toArray(new String[_confReports.size()]);
    }
    public boolean hasError() {
        boolean hasError = false;
        for (Iterator it = _confReports.values().iterator(); it.hasNext() && !hasError;) {
            ConfigurationResolveReport report = (ConfigurationResolveReport)it.next();
            hasError |= report.hasError();
        }
        return hasError;
    }

    public void output(ReportOutputter[] outputters, File cache) {
        for (int i = 0; i < outputters.length; i++) {
            outputters[i].output(this, cache);
        }
    }
    
    public ModuleDescriptor getModuleDescriptor() {
        return _md;
    }
    
    public IvyNode[] getEvictedNodes() {
        Collection all = new HashSet();
        for (Iterator iter = _confReports.values().iterator(); iter.hasNext();) {
            ConfigurationResolveReport report = (ConfigurationResolveReport)iter.next();
            all.addAll(Arrays.asList(report.getEvictedNodes()));
        }
        return (IvyNode[])all.toArray(new IvyNode[all.size()]);
    }
    public IvyNode[] getUnresolvedDependencies() {
        Collection all = new HashSet();
        for (Iterator iter = _confReports.values().iterator(); iter.hasNext();) {
            ConfigurationResolveReport report = (ConfigurationResolveReport)iter.next();
            all.addAll(Arrays.asList(report.getUnresolvedDependencies()));
        }
        return (IvyNode[])all.toArray(new IvyNode[all.size()]);
    }
    public ArtifactDownloadReport[] getFailedArtifactsReports() {
        Collection all = new HashSet();
        for (Iterator iter = _confReports.values().iterator(); iter.hasNext();) {
            ConfigurationResolveReport report = (ConfigurationResolveReport)iter.next();
            all.addAll(Arrays.asList(report.getFailedArtifactsReports()));
        }
        return (ArtifactDownloadReport[])all.toArray(new ArtifactDownloadReport[all.size()]);
    }
	public boolean hasChanged() {
        for (Iterator iter = _confReports.values().iterator(); iter.hasNext();) {
            ConfigurationResolveReport report = (ConfigurationResolveReport)iter.next();
			if (report.hasChanged()) {
				return true;
			}
        }
		return false;
	}
}
