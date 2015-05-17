package nl.b3p.commons.stripes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import net.sourceforge.stripes.tag.DefaultPopulationStrategy;
import net.sourceforge.stripes.tag.PopulationStrategy;

/**
 * From: http://www.stripesframework.org/display/stripes/Overriding+PopulationStrategy+per+ActionBean
 * @author Meine Toonen meinetoonen@b3partners.nl
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomPopulationStrategy 
{
  Class<? extends PopulationStrategy> value() 
    default DefaultPopulationStrategy.class;
}