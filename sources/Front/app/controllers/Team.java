package controllers;

import models.Reference;
import models.TeamMember;
import play.mvc.Controller;
import configuration.Configurator;
import factories.IReferencesFactory;
import factories.ITeamMemberFactory;

public class Team extends Controller
{
	public static void run() {
		TeamMember[] allMembers = Configurator.getContainer().get(ITeamMemberFactory.class).getAllMembers();
		Reference[] allReferences = Configurator.getContainer().get(IReferencesFactory.class).select();
		render("Team/team.html", allMembers, allReferences);
	}

}
