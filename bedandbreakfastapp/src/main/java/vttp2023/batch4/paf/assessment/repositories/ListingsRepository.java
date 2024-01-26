package vttp2023.batch4.paf.assessment.repositories;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import vttp2023.batch4.paf.assessment.Utils;
import vttp2023.batch4.paf.assessment.models.Accommodation;
import vttp2023.batch4.paf.assessment.models.AccommodationSummary;

@Repository
public class ListingsRepository {
	
	// You may add additional dependency injections

	@Autowired
	private MongoTemplate template;

	/*
		db.listings.aggregate([
			{
				$group: {
					_id: "$address.suburb",
				}
			},
			{
				$sort: {_id: 1}
			}
		])
	 */
	public List<String> getSuburbs(String country) {
		GroupOperation groupOperation = Aggregation.group("address.suburb");
		SortOperation sortOperation = Aggregation.sort(Sort.by(Direction.ASC, "_id"));
		Aggregation pipeline = Aggregation.newAggregation(groupOperation, sortOperation);
		AggregationResults<Document> results = template.aggregate(pipeline, "listings", Document.class);
        List<String> suburbs = new LinkedList<>();
        for(Document d : results){
            String suburb = d.getString("_id");
			if(suburb.isEmpty()){
				continue;
			}
            // System.out.println("SUBURB: " + suburb);
            suburbs.add(suburb);
        }
		return suburbs;
	}

	/*
		db.listings.find({
			"address.suburb": {$regex:"alexandria", $options:"i"},
			accommodates: {$gte: 4},
			price:{$gte: 50, $lte: 800},
			min_nights: {$lte: 10 }
		},
		{
			name:1, "address.suburb":1, accommodates: 1, price:1, min_nights:1
		}).sort({price: -1});
	 */
	public List<AccommodationSummary> findListings(String suburb, int persons, int duration, float priceRange) {
		Query query = new Query(
			Criteria.where("address.suburb").regex(suburb, "i")
					.and("accommodates").gte(persons)
					.and("price").gte(50).lte(priceRange)
					.and("min_nights").lte(duration)
		);
		query.fields().include("name", "accommodates", "price");
        query.with(Sort.by(Direction.DESC, "price"));
		List<Document> result = template.find(query, Document.class, "listings");
		// System.out.println("DOC " + result.toString());
		List<AccommodationSummary> listings = new LinkedList<>();
		for (Document d : result) {
			AccommodationSummary as = new AccommodationSummary();
            as.setId(d.getString("_id"));
            as.setName(d.getString("name"));
			as.setAccomodates(d.getInteger("accommodates"));
            as.setPrice(d.get("price", Number.class).floatValue());
			// System.out.println(as.getPrice());
            listings.add(as);
        }

		return listings;
	}

	// IMPORTANT: DO NOT MODIFY THIS METHOD UNLESS REQUESTED TO DO SO
	// If this method is changed, any assessment task relying on this method will
	// not be marked
	public Optional<Accommodation> findAccommodatationById(String id) {
		Criteria criteria = Criteria.where("_id").is(id);
		Query query = Query.query(criteria);

		List<Document> result = template.find(query, Document.class, "listings");
		if (result.size() <= 0)
			return Optional.empty();

		return Optional.of(Utils.toAccommodation(result.getFirst()));
	}

}
