/**
 * UnitTrayLabel.java
 * edu.harvard.mcz.precapture.encoder
 * Copyright © 2009 President and Fellows of Harvard College
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of Version 2 of the GNU General Public License
 * as published by the Free Software Foundation.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * Author: Paul J. Morris
 */
package edu.harvard.mcz.precapture.data;

import edu.harvard.mcz.precapture.interfaces.TaxonNameReturner;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;


/**
 * UnitTrayLabel
 * <p>
 * Includes factory method for decoding a set of key-value pairs in JSON, and a method for constructing
 * such a string with a JSON encoding.
 *
 * @author Paul J. Morris
 */
public class UnitTrayLabel implements TaxonNameReturner {

    private static final Log log = LogFactory.getLog(UnitTrayLabel.class);

    private Long id;
    private String drawerNumber;
    private String family;
    private String subfamily;
    private String tribe;
    private String scientificName;
    private String genus;
    private String specificEpithet;
    private String subspecificEpithet;
    private String infraspecificEpithet;
    private String infraspecificRank;
    private String authorship;
    private String unNamedForm;
    private int printed;
    private int numberToPrint;
    private Date dateCreated;
    private Date dateLastUpdated;
    private String collection;  // collection from which the material came
    private String identifiedBy;
    private String identifiedDate;
    private Integer ordinal;     // order in which to print
    private String sex;

    public UnitTrayLabel() {
        this.printed = 0;
        this.numberToPrint = 0;
        this.drawerNumber = "";
        this.family = "";
        this.subfamily = "";
        this.tribe = "";
        this.genus = "";
        this.specificEpithet = "";
        this.subspecificEpithet = "";
        this.infraspecificEpithet = "";
        this.infraspecificRank = "";
        this.authorship = "";
        this.unNamedForm = "";
        this.collection = "";
        this.identifiedBy = "";
        this.sex = "";
        this.ordinal = 1;
    }

    /**
     * Constructor with all fields
     *
     * @param drawerNumber
     * @param family
     * @param subfamily
     * @param tribe
     * @param genus
     * @param specificEpithet
     * @param subspecificEpithet
     * @param infraspecificEpithet
     * @param infraspecificRank
     * @param authorship
     * @param unnamedForm
     * @param printed
     * @param collection
     * @param sex
     */
    public UnitTrayLabel(Long id, String drawerNumber, String family, String subfamily,
                         String tribe, String genus, String specificEpithet, String subspecificEpithet,
                         String infraspecificEpithet, String infraspecificRank,
                         String authorship, String unnamedForm,
                         int printed, Date dateCreated, Date dateLastUpdated, String collection, String identifiedBy, Integer ordinal, String sex) {
        super();
        this.id = id;
        this.drawerNumber = drawerNumber;
        this.family = family;
        this.subfamily = subfamily;
        this.tribe = tribe;
        this.genus = genus;
        this.specificEpithet = specificEpithet;
        this.subspecificEpithet = subspecificEpithet;
        this.infraspecificEpithet = infraspecificEpithet;
        this.infraspecificRank = infraspecificRank;
        this.authorship = authorship;
        this.unNamedForm = unnamedForm;
        this.printed = printed;
        this.dateCreated = dateCreated;
        this.dateLastUpdated = dateLastUpdated;
        this.collection = collection;
        this.identifiedBy = identifiedBy;
        this.ordinal = ordinal;
        this.sex = sex;
    }

    /**
     * Constructor with all fields
     *
     * @param drawerNumber
     * @param family
     * @param subfamily
     * @param tribe
     * @param genus
     * @param specificEpithet
     * @param subspecificEpithet
     * @param infraspecificEpithet
     * @param infraspecificRank
     * @param authorship
     * @param unnamedForm
     * @param printed
     * @param collection
     */
    public UnitTrayLabel(Long id, String drawerNumber, String family, String subfamily,
                         String tribe, String genus, String specificEpithet, String subspecificEpithet,
                         String infraspecificEpithet, String infraspecificRank,
                         String authorship, String unnamedForm,
                         int printed, Date dateCreated, Date dateLastUpdated, String collection, String identifiedBy, Integer ordinal) {
        super();
        this.id = id;
        this.drawerNumber = drawerNumber;
        this.family = family;
        this.subfamily = subfamily;
        this.tribe = tribe;
        this.genus = genus;
        this.specificEpithet = specificEpithet;
        this.subspecificEpithet = subspecificEpithet;
        this.infraspecificEpithet = infraspecificEpithet;
        this.infraspecificRank = infraspecificRank;
        this.authorship = authorship;
        this.unNamedForm = unnamedForm;
        this.printed = printed;
        this.dateCreated = dateCreated;
        this.dateLastUpdated = dateLastUpdated;
        this.collection = collection;
        this.identifiedBy = identifiedBy;
        this.ordinal = ordinal;
    }

    /**
     * Constructor for infraspcific trinomial with explicit rank.
     *
     * @param drawerNumber
     * @param family
     * @param subfamily
     * @param tribe
     * @param genus
     * @param specificEpithet
     * @param infraspecificEpithet
     * @param infraspecificRank
     * @param authorship
     * @param collection
     * @param identifiedBy
     */
    public UnitTrayLabel(String drawerNumber, String family, String subfamily,
                         String tribe, String genus, String specificEpithet,
                         String infraspecificEpithet, String infraspecificRank,
                         String authorship, String collection, String identifiedBy) {
        super();
        this.drawerNumber = drawerNumber;
        this.family = family;
        this.subfamily = subfamily;
        this.tribe = tribe;
        this.genus = genus;
        this.specificEpithet = specificEpithet;
        this.subspecificEpithet = "";
        this.infraspecificEpithet = infraspecificEpithet;
        this.infraspecificRank = infraspecificRank;
        this.authorship = authorship;
        this.collection = collection;
        this.identifiedBy = identifiedBy;
    }

    /**
     * Constructor for species
     *
     * @param drawerNumber
     * @param family
     * @param subfamily
     * @param tribe
     * @param genus
     * @param specificEpithet
     * @param authorship
     * @param collection
     * @param identifiedBy
     */
    public UnitTrayLabel(String drawerNumber, String family, String subfamily,
                         String tribe, String genus, String specificEpithet, String subspecificEpithet, String infraspecificEpithet, String infraspecificRank,
                         String authorship, String collection, String identifiedBy) {
        super();
        this.drawerNumber = drawerNumber;
        this.family = family;
        this.subfamily = subfamily;
        this.tribe = tribe;
        this.genus = genus;
        this.specificEpithet = specificEpithet;
        this.subspecificEpithet = subspecificEpithet;
        this.infraspecificEpithet = infraspecificEpithet;
        this.infraspecificRank = infraspecificRank;
        this.authorship = authorship;
        this.collection = collection;
        this.identifiedBy = identifiedBy;
    }

    /**
     * Returns a string containing the taxon name used in this UnitTrayLabel.
     */
    public String toString() {
        return this.getScientificName();
    }

    /**
     * @return the drawerNumber
     */
    public String getDrawerNumber() {
        return drawerNumber;
    }

    /**
     * @param drawerNumber the drawerNumber to set
     */
    public void setDrawerNumber(String drawerNumber) {
        this.drawerNumber = drawerNumber;
        if (this.drawerNumber != null) {
            this.drawerNumber = this.drawerNumber.trim();
        }
    }

    /**
     * @return the family
     */
    public String getFamily() {
        return family;
    }

    /**
     * @param family the family to set
     */
    public void setFamily(String family) {
        this.family = family;
        if (this.family != null) {
            this.family = this.family.trim();
        }
    }

    /**
     * @return the subfamily
     */
    public String getSubfamily() {
        return subfamily;
    }

    /**
     * @param subfamily the subfamily to set
     */
    public void setSubfamily(String subfamily) {
        this.subfamily = subfamily;
        if (this.subfamily != null) {
            this.subfamily = this.subfamily.trim();
        }
    }

    /**
     * @return the tribe
     */
    public String getTribe() {
        if (tribe == null) {
            tribe = "";
        }
        return tribe;
    }

    /**
     * @param tribe the tribe to set
     */
    public void setTribe(String tribe) {
        this.tribe = tribe;
        if (this.tribe != null) {
            this.tribe = this.tribe.trim();
        }
    }

    /**
     * @return the scientificName
     */
    public String getScientificName() {
        if (this.scientificName != null && !this.scientificName.trim().equals("")) {
            return scientificName;
        }
        this.scientificName = UnitTrayLabelLifeCycle.getScientificName(this);
        return this.scientificName;
    }

    /**
     * @param scientificName the scientificName to set
     */
    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    /**
     * @return the genus
     */
    public String getGenus() {
        if (genus == null) {
            genus = "";
        }
        return genus;
    }

    /**
     * @param genus the genus to set
     */
    public void setGenus(String genus) {
        this.genus = genus;
        if (this.genus != null) {
            this.genus = this.genus.trim();
        }
    }

    /**
     * @return the specificEpithet
     */
    public String getSpecificEpithet() {
        if (specificEpithet == null) {
            specificEpithet = "";
        }
        return specificEpithet;
    }

    /**
     * @param specificEpithet the specificEpithet to set
     */
    public void setSpecificEpithet(String specificEpithet) {
        this.specificEpithet = specificEpithet;
        if (this.specificEpithet != null) {
            this.specificEpithet = this.specificEpithet.trim();
        }
    }

    /**
     * @return the subspecificEpithet
     */
    public String getSubspecificEpithet() {
        if (subspecificEpithet == null) {
            subspecificEpithet = "";
        }
        return subspecificEpithet;
    }

    /**
     * @param subspecificEpithet the subspecificEpithet to set
     */
    public void setSubspecificEpithet(String subspecificEpithet) {
        this.subspecificEpithet = subspecificEpithet;
        if (this.subspecificEpithet != null) {
            this.subspecificEpithet = this.subspecificEpithet.trim();
        }
    }

    /**
     * @return the infraspecificEpithet
     */
    public String getInfraspecificEpithet() {
        if (infraspecificEpithet == null) {
            infraspecificEpithet = "";
        }
        return infraspecificEpithet;
    }

    /**
     * @param infraspecificEpithet the infraspecificEpithet to set
     */
    public void setInfraspecificEpithet(String infraspecificEpithet) {
        this.infraspecificEpithet = infraspecificEpithet;
        if (this.infraspecificEpithet != null) {
            this.infraspecificEpithet = this.infraspecificEpithet.trim();
        }
    }

    /**
     * @return the infraspecifcRank
     */
    public String getInfraspecificRank() {
        if (infraspecificRank == null) {
            infraspecificRank = "";
        }
        return infraspecificRank;
    }

    /**
     * @param infraspecifcRank the infraspecifcRank to set
     */
    public void setInfraspecificRank(String infraspecifcRank) {
        this.infraspecificRank = infraspecifcRank;
        if (this.infraspecificRank != null) {
            this.infraspecificRank = this.infraspecificRank.trim();
        }
    }

    /**
     * @return the authorship
     */
    public String getAuthorship() {
        if (authorship == null) {
            authorship = "";
        }
        return authorship;
    }

    /**
     * @param authorship the authorship to set
     */
    public void setAuthorship(String authorship) {
        this.authorship = authorship;
        if (this.authorship != null) {
            this.authorship = this.authorship.trim();
        }
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the unnamedForm
     */
    public String getUnNamedForm() {
        if (unNamedForm == null) {
            unNamedForm = "";
        }
        return unNamedForm;
    }

    /**
     * @param unNamedForm the unnamedForm to set
     */
    public void setUnNamedForm(String unNamedForm) {
        this.unNamedForm = unNamedForm;
    }

    public int getPrinted() {
        return printed;
    }

    public void setPrinted(int printed) {
        this.printed = printed;
    }

    public int getNumberToPrint() {
        return this.numberToPrint;
    }

    public void setNumberToPrint(int numberToPrint) {
        this.numberToPrint = numberToPrint;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateLastUpdated() {
        return dateLastUpdated;
    }

    public void setDateLastUpdated(Date dateLastUpdated) {
        this.dateLastUpdated = dateLastUpdated;
    }

    public String getCollection() {
        return collection;
    }

    /**
     * @param collection the collection to set
     */
    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getIdentifiedBy() {
        return identifiedBy;
    }

    /**
     * @param identifiedBy the name of the identifier
     */
    public void setIdentifiedBy(String identifiedBy) {
        this.identifiedBy = identifiedBy;
    }

    public String getIdentifiedDate() {
        return identifiedDate;
    }

    public void setIdentifiedDate(String identifiedDate) {
        this.identifiedDate = identifiedDate;
    }

    /**
     * @return the ordinal
     */
    public Integer getOrdinal() {
        if (ordinal == null) {
            ordinal = new Integer(0);
        }
        return ordinal;
    }

    /**
     * @param ordinal the ordinal to set
     */
    public void setOrdinal(Integer ordinal) {
        if (ordinal == null) {
            this.ordinal = new Integer(0);
        } else {
            this.ordinal = ordinal;
        }
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
