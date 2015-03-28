package factories;

import models.TeamMember;

public class TeamMemberFactory implements ITeamMemberFactory
{
	private static TeamMember[] allMembers = {
												new TeamMember("Lesha Khvorost", "18.08.1985", "jaamal@mail.ru", "assistant, Ural Federal University", "lesha.jpg"),
												new TeamMember("Ivan Burmistrov", "22.12.1987", "burmistrov.ivan@gmail.com", "post-graduate student, Ural Federal University", "ivan_.jpg"),
												new TeamMember("Yury Pliner", "24.01.1991", "yury.pliner@googlemail.com", "student, Ural Federal University", "yury_.jpg"),
												new TeamMember("Eugene Kurpilyansky", "04.02.1993", "DembelZ@yandex.ru", "student, Ural Federal University", "evgen_.jpg"),
												new TeamMember("Sofia Tekhazeva", "22.01.1992", "sofia.tex@yandex.ru", "student, Ural Federal University", "sofia.jpg"),
												new TeamMember("Den Muhametianov", "23.06.1992", "Den.Mukhametianov@gmail.com", "student, Ural Federal University", "den.jpg"),
												new TeamMember("Anna Kozlova", "13.02.1991", "voron13e02@gmail.com", "student, Ural Federal University", "anna.jpg")
											 };
	
	@Override
	public TeamMember[] getAllMembers()
	{
		return allMembers;
	}
}
