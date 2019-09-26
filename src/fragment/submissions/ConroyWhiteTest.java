package fragment.submissions;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import fragment.submissions.ConroyWhite.AssemleLine;

class ConroyWhiteTest {

	private ConroyWhite.AssemleLine conroy = new ConroyWhite.AssemleLine();

	@Test
	void reassembleLineTest() throws FileNotFoundException, IOException {
		String expected = "Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem.";
		String actual = "";
		BufferedReader in = new BufferedReader(new FileReader("file.txt"));
		Stream<String> fragmentProblem = in.lines().map(AssemleLine::reassembleLine);
		List<String> result = fragmentProblem.collect(Collectors.toList());
		for (String s : result) {
			actual = s;
		}
		assertThat(expected, is(actual));
	}

}
