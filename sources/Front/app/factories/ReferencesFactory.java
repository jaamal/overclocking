package factories;

import models.Reference;

public class ReferencesFactory implements IReferencesFactory
{

	private static final Reference[] allRefences = new Reference[] {
		new Reference("L. Khvorost", "Searching all pure squares in compressed tests", "Proc. Int. Conf. AutoMathA: from Math. To App., AutoMathA (2009)", "http://overclocking.googlecode.com/svn/texfiles/Khvorost/saps/automatha_2009/paper/paper.pdf"),
		new Reference("I. Burmistrov, L. Khvorost", "Straight-line programs: a practical test", "Proc. Int. Conf. Data Compression, Commun., Process., CCP (2011), 76-81", "http://overclocking.googlecode.com/svn/texfiles/Khvorost/slps_test/ccp_2011/paper/paper.pdf"),
		new Reference("I. Burmistrov, L. Khvorost, A. Kozlova, E. Kurpilyansky", "Straight-line programs: a practical test (extended abstract)", "to be announced", "")
	};
	
	@Override
	public Reference[] select() {
		return allRefences;
	}
}
