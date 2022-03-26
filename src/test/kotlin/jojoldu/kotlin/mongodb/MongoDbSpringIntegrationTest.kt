package jojoldu.kotlin.mongodb

import com.mongodb.BasicDBObjectBuilder
import com.mongodb.DBObject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.test.context.junit.jupiter.SpringExtension


@DataMongoTest
@ExtendWith(SpringExtension::class)
class MongoDbSpringIntegrationTest {
  @DisplayName("given object to save"
      + " when save object using MongoDB template"
      + " then object is saved")
  @Test
  fun test(@Autowired mongoTemplate: MongoTemplate) {
    // given
    val objectToSave = BasicDBObjectBuilder.start()
        .add("key", "value")
        .get()

    // when
    mongoTemplate.save(objectToSave, "collection")

    // then
    assertThat(mongoTemplate.findAll(DBObject::class.java, "collection")).extracting("key")
        .containsOnly("value")
  }
}