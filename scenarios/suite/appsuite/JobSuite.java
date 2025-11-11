package suite.appsuite;

import org.testng.annotations.Test;

import pages.Job;
import suite.basesuite.JobBasesuite;
import utils.Result;

public class JobSuite extends JobBasesuite {

	@Test
	public void jobAutomation() {
		Job job = new Job();
		job.test();
	}
}
