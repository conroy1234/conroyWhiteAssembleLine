package fragment.submissions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConroyWhite {
	public static void main(String[] args) throws IOException {
		args = new String[1];
		args[0]="file.txt";
		// instantiate assemble line to reassembled fragments
		AssemleLine assemble = new AssemleLine();
		// read the test value from test file and defragment the values
		try (BufferedReader in = new BufferedReader(new FileReader(args[0]))) {
		
		in
		.lines()
		.map(AssemleLine::reassembleLine).forEach(System.out::println);

		}
	}
	

/*
 * fragment entity with
 */
static class Fragments {
	public String fragment = "";
	public String data = "";
	public Match match = null;
	public String attachFragments;

	public String getFrag1() {
		return fragment;
	}

	public void setFrag1(String frag1) {
		this.fragment = frag1;
	}

	public String getFrag2() {
		return data;
	}

	public void setFrag2(String frag2) {
		this.data = frag2;
	}

	public Match getMatch() {
		return match;
	}

	public void setMatch(Match match) {
		this.match = match;
	}

	public String getMergedFragment() {
		return attachFragments;
	}

	public void setMergedFragment(String mergedFragment) {
		this.attachFragments = mergedFragment;
	}

	@Override
	public String toString() {
		return "Fragments [frag1=" + fragment + ", frag2=" + data + ", mergedFragment=" + attachFragments + "]";
	}

}

static class Match {
	public Integer xvalue = 0;
	public Integer yvalue = 0;
	public Integer maxValue = 0;
	public String value;

	public Integer getXvalue() {
		return xvalue;
	}

	public void setXvalue(Integer xvalue) {
		this.xvalue = xvalue;
	}

	public Integer getYvalue() {
		return yvalue;
	}

	public void setYvalue(Integer yvalue) {
		this.yvalue = yvalue;
	}

	public Integer getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Integer maxValue) {
		this.maxValue = maxValue;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Match [xvalue=" + xvalue + ", yvalue=" + yvalue + ", maxValue=" + maxValue + ", value=" + value + "]";
	}

}

static class FragmentException extends RuntimeException {
	public FragmentException(String message) {
		super(message);
	}
}

static class FragmentFactory {

	private static FragmentFactory instance;

	private FragmentFactory() {

	}

	public static FragmentFactory getInstance() {
		instance = new FragmentFactory();
		return instance;
	}

	/*
	 * assemble factory that help assemble fragments
	 */
	public String assembleFactory(List<Fragments> fragments, Fragments entity, String fragement, int fragmentCount) {
		StringBuilder buffer = new StringBuilder();
		boolean isThereAnyFragment = false;
		for (int i = 0; i < fragments.size(); i++) {
			Fragments currentEntity = fragments.get(i);
			if (i != 0) {
				buffer.append(";");
			}
			if ((fragmentCount == i) && (entity.attachFragments.length() > 0)) {
				buffer.append(entity.attachFragments);
				isThereAnyFragment = true;
			} else {

				if ((!isThereAnyFragment) && ((i + 1) == fragments.size())) {
					buffer.append(currentEntity.fragment).append(";").append(currentEntity.data);
				} else {
					if (isThereAnyFragment) {
						buffer.append(currentEntity.data);
					} else {
						buffer.append(currentEntity.fragment);
					}
				}
			}
		}
		return buffer.toString();
	}

	public boolean isFragmentOneBeginingMatchesTheEndOfFragmentTwo(Fragments fragment) {

		if (fragment.fragment.startsWith(fragment.match.value) && fragment.data.endsWith(fragment.match.value)) {
			return true;
		}

		return false;
	}

	public boolean isFragmentsCombinedEnded(Fragments fragment) {

		if (fragment.fragment.endsWith(fragment.match.value) && fragment.data.startsWith(fragment.match.value)) {
			return true;
		}
		return false;
	}

	public boolean isFragmentsCombinedCompleted(Fragments fragment) {

		if (isFragmentOneBeginingMatchesTheEndOfFragmentTwo(fragment)) {
			return true;
		}

		if (isFragmentsCombinedEnded(fragment)) {
			return true;
		}
		return false;
	}

	public boolean removeFragment(Fragments fragment) {

		if (fragment.match.value.equals(fragment.fragment) || (fragment.match.value.equals(fragment.data))) {
			return true;
		}

		return false;
	}

	public boolean isThereanyValigMatchFound(Fragments fragment) {

		if (isFragmentsCombinedCompleted(fragment)) {
			return true;
		}

		if (removeFragment(fragment)) {
			return true;
		}

		return false;
	}

	public String isCombine(Fragments fragment, String localFragment, String localFragmentData) {
		String newFragment;
		if (isFragmentsCombinedEnded(fragment)) {
			newFragment = localFragment.substring(0, localFragment.length() - fragment.match.value.length());
			newFragment = newFragment + localFragmentData;
		} else {
			newFragment = localFragmentData.substring(0, localFragmentData.length() - fragment.match.value.length());
			newFragment = newFragment + localFragment;
		}
		return newFragment;
	}

	public void checkFragmentState(String[] fragments, int i, Fragments fragment) {
		if (i == 0) {
			fragment.fragment = fragments[0];
			fragment.data = fragments[1];
		} else {
			fragment.fragment = fragments[i];
			fragment.data = fragments[i + 1];

		}
	}

	public String checkFragmentsLength(String localFragment, String localFragmentData) {
		String newFragment;
		if (localFragment.length() > localFragmentData.length()) {
			newFragment = localFragment;
		} else if (localFragmentData.length() > localFragment.length()) {
			newFragment = localFragmentData;
		} else {
			newFragment = localFragmentData;
		}
		return newFragment;
	}
}

static class OverLappingFragments {

	/*
	 * find overlapping fragments
	 */
	public Fragments findAllOverLappingFragments(Fragments fragmentPair) {

		Integer[][] matchingMatrix = assembleMatchingFragments(fragmentPair.fragment, fragmentPair.data);
		Match match = findLocationOfLongestMatchingFragments(matchingMatrix);

		if (match.maxValue > 0) {
			match.value = searchForMatchingFragments(match, fragmentPair.fragment);
		} else {
			match.value = "";
		}
		fragmentPair.match = match;
		return fragmentPair;
	}

	/*
	 * search for matching fragments
	 */
	public String searchForMatchingFragments(Match match, String target) {
		int currLoc = match.xvalue;
		StringBuilder subString = new StringBuilder();
		for (int i = 0; i < match.maxValue; i++) {
			subString.append(target.charAt(currLoc));
			currLoc = currLoc - 1;
		}
		return subString.reverse().toString();
	}

	/*
	 * find location Of longest matching fragments and return the matches
	 */
	public Match findLocationOfLongestMatchingFragments(Integer[][] payload) {

		int maxVal = 0;
		Match match = new Match();
		for (int i = 0; i < payload.length; i++) {
			for (int j = 0; j < payload[0].length; j++) {
				Integer val = payload[i][j];
				if (val > maxVal) {
					match.xvalue = i;
					match.yvalue = j;
					match.maxValue = val;
					maxVal = val;
				}
			}
		}

		return match;
	}

	/*
	 * build matching fragments
	 */
	public Integer[][] assembleMatchingFragments(String target, String data) {
		if ((target == null) || (target.length() < 1) || (data == null) || (data.length() < 1)) {
			return null;
		}

		Integer[][] matchFragment = new Integer[target.length()][data.length()];
		for (int i = 0; i < target.length(); i++) {
			for (int j = 0; j < data.length(); j++) {
				if (target.charAt(i) != data.charAt(j)) {
					matchFragment[i][j] = 0;
				} else {
					if ((i == 0) || (j == 0))
						matchFragment[i][j] = 1;
					else
						matchFragment[i][j] = 1 + matchFragment[i - 1][j - 1];

				}
			}
		}
		return matchFragment;
	}

}

static class AssemleLine {

	static FragmentFactory fragmentFactory = FragmentFactory.getInstance();

	private static String reassembleFragments(List<String> val) {
		StringBuilder sb = new StringBuilder();
		sb.append(val.get(0));
		for (String str : val) {
			sb.append(";").append(str);
		}
		return sb.toString();
	}

	public static String sortAndAssemble(String fragment, Integer responce, Integer request) {

		String results[] = fragment.split(";");
		ArrayList<String> collectionPayload = new ArrayList<>(Arrays.asList(results));

		String payload = collectionPayload.remove(responce.intValue());
		collectionPayload.add(request, payload);
		return reassembleFragments(collectionPayload);

	}

	private static String[] splitValues(String valueToSplit) {
		return valueToSplit.split(";");
	}

	/*
	 * reassemble fragments
	 */
	public static String reassembleLine(String fragment) {

		String results = defragment(fragment);
		boolean isSorted = false;

		int previousSize = splitValues(results).length;
		int sortedFragments = previousSize - 1;
		if (previousSize > 1) {
			int responce = previousSize - 1;
			int request = 1;
			while (true) {
				results = defragment(fragment);
				if (splitValues(results).length > 1) {
					int currentSize = splitValues(results).length;
					if (currentSize == previousSize) {

						// chect if ther are matches
						if ((responce == sortedFragments) && (isSorted)) {
							sortedFragments = sortedFragments - 1;
							// compare all the matches to see if there are repeated
							if (sortedFragments == 0) {
								break;
							} else {
								responce = sortedFragments;
								request = 1;
							}
						} else {
							if (isSorted) {
								responce = request;
								request = request + 1;
							} else {
								isSorted = true;
							}
						}

						fragment = sortAndAssemble(results, responce, request);
					} else {
						previousSize = currentSize;
						sortedFragments = previousSize - 1;
						responce = previousSize - 1;
						request = 1;
						isSorted = false;
					}

				} else {

					break;
				}
			}
		}
		return results;
	}

	/*
	 * decompose the fragments
	 */
	public static String defragment(String fragment) {

		StringBuffer buffer = new StringBuffer(fragment);
		String fragments[] = buffer.toString().split(";");
		int currentFragmentsCount = fragments.length;
		int previousFragmentsCount = currentFragmentsCount;

		while (currentFragmentsCount > 1) {

			String combined = assembleTogetherLargeFragments(buffer.toString());
			buffer = new StringBuffer(combined);
			fragments = buffer.toString().split(";");
			currentFragmentsCount = fragments.length;

			if ((currentFragmentsCount == previousFragmentsCount) && (currentFragmentsCount > 1)) {
				break;
			} else {
				previousFragmentsCount = currentFragmentsCount;
			}

		}
		return buffer.toString();

	}

	/*
	 * assemble fragments together
	 */
	public static String assembleTogetherLargeFragments(String fragement) {

		List<Fragments> fragments = splitFragmentsIntoPairs(fragement);
		int fragmentCount = LocateTheLongestMatches(fragments);
		Fragments entity = getFragmentsFromEntity(fragments.get(fragmentCount));
		return fragmentFactory.assembleFactory(fragments, entity, fragement, fragmentCount);
	}

	/*
	 * populate entity with fragments
	 */
	public static Fragments getFragmentsFromEntity(Fragments fragment) {

		String localFragment = fragment.fragment;
		String localFragmentData = fragment.data;

		String newFragment = "";
		Boolean isFragmentsCombined = fragmentFactory.isFragmentsCombinedCompleted(fragment);
		if (isFragmentsCombined) {

			newFragment = fragmentFactory.isCombine(fragment, localFragment, localFragmentData);
		} else if (fragmentFactory.removeFragment(fragment)) {

			newFragment = fragmentFactory.checkFragmentsLength(localFragment, localFragmentData);
		} else {
			newFragment = "";
		}
		fragment.attachFragments = newFragment;

		return fragment;

	}

	/*
	 * locate the longest matches and return fragment count
	 */
	public static int LocateTheLongestMatches(List<Fragments> fragments) {

		int max = 0;
		int count = 0;

		for (int i = 0; i < fragments.size(); i++) {
			Fragments fragment = fragments.get(i);
			if (fragment.match.value.length() > max) {
				if (fragmentFactory.isThereanyValigMatchFound(fragment) || fragmentFactory.removeFragment(fragment)) {
					max = fragment.match.value.length();
					count = i;
				}
			}
		}
		return count;
	}

	/*
	 * split fragments using and remove the semicolon from file data
	 */
	public static List<Fragments> splitFragmentsIntoPairs(String fragmenttedString) {
		String[] fragments = fragmenttedString.split(";");
		if (fragments.length < 2) {
			throw new FragmentException("cannot perform opperstion");
		}

		List<Fragments> fragmentPairs = findFragments(fragments);
		return fragmentPairs;

	}

	/*
	 * find the collection of fragments and return them
	 */
	private static List<Fragments> findFragments(String[] fragments) {
		List<Fragments> fragmentList = new ArrayList<>();

		for (int i = 0; (i + 1) < fragments.length; ++i) {
			Fragments fragment = new Fragments();

			fragmentFactory.checkFragmentState(fragments, i, fragment);

			OverLappingFragments overlapping = new OverLappingFragments();
			fragmentList.add(overlapping.findAllOverLappingFragments(fragment));

		}
		return fragmentList;
	}

}
}
