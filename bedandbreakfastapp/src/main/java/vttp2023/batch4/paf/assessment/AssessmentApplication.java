package vttp2023.batch4.paf.assessment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import vttp2023.batch4.paf.assessment.models.User;
import vttp2023.batch4.paf.assessment.repositories.BookingsRepository;
import vttp2023.batch4.paf.assessment.repositories.ListingsRepository;
import vttp2023.batch4.paf.assessment.services.ForexService;

@SpringBootApplication
public class AssessmentApplication implements CommandLineRunner {

	@Autowired
	ListingsRepository listingsRepo;

	@Autowired
	ForexService forexSvc;

	@Autowired
	BookingsRepository bookingsRepo;

	public static void main(String[] args) {
		SpringApplication.run(AssessmentApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// listingsRepo.getSuburbs("australia");
		// listingsRepo.findListings("Alexandria", 4, 10, 800);
		// System.out.println(forexSvc.convert("AUD", "SGD", 151));
		// bookingsRepo.newUser(new User("abc@gmail.com", "abc"));
	}
}
