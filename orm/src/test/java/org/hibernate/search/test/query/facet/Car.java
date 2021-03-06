/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

package org.hibernate.search.test.query.facet;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.lucene.analysis.core.KeywordTokenizerFactory;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.AnalyzerDefs;
import org.hibernate.search.annotations.Facet;
import org.hibernate.search.annotations.FacetEncodingType;
import org.hibernate.search.annotations.Facets;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;
import org.hibernate.search.bridge.builtin.IntegerBridge;

/**
 * @author Hardy Ferentschik
 */
@Entity
@Indexed
@AnalyzerDefs({
	@AnalyzerDef(
			name = "collatingAnalyzer",
			tokenizer = @TokenizerDef(factory = KeywordTokenizerFactory.class),
			filters = {
					@TokenFilterDef(factory = ASCIIFoldingFilterFactory.class),
					@TokenFilterDef(factory = LowerCaseFilterFactory.class)
			}
	)
})
public class Car {
	@Id
	@GeneratedValue
	private int id;

	@Field(analyze = Analyze.NO)
	@Facet
	private String color;

	@Fields({
		@Field(analyze = Analyze.NO, store = Store.YES),
		@Field(name = "facetNameCollision", store = Store.YES, analyzer = @Analyzer(definition = "collatingAnalyzer"))
	})
	@Facets({
		@Facet,
		@Facet(name = "facetNameCollision")
	})

	private String make;

	@Field(name = "cubicCapacity", analyze = Analyze.NO, bridge = @FieldBridge(impl = IntegerBridge.class))
	@Facets({
			@Facet(name = "cubicCapacity", forField = "cubicCapacity", encoding = FacetEncodingType.STRING),
			@Facet(name = "cubicCapacity_Numeric", forField = "cubicCapacity", encoding = FacetEncodingType.LONG)
	})
	private Integer cubicCapacity;

	public Car() {
	}

	public Car(String make, String color, Integer cubicCapacity) {
		this.color = color;
		this.cubicCapacity = cubicCapacity;
		this.make = make;
	}

	public String getColor() {
		return color;
	}

	public Integer getCubicCapacity() {
		return cubicCapacity;
	}

	public int getId() {
		return id;
	}

	public String getMake() {
		return make;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append( "Car" );
		sb.append( "{id=" ).append( id );
		sb.append( ", color='" ).append( color ).append( '\'' );
		sb.append( ", make='" ).append( make ).append( '\'' );
		sb.append( ", cubicCapacity=" ).append( cubicCapacity );
		sb.append( '}' );
		return sb.toString();
	}
}


