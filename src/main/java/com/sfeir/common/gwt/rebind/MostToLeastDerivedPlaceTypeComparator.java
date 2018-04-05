package com.sfeir.common.gwt.rebind;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.thirdparty.guava.common.collect.ComparisonChain;
import java.util.Comparator;

public class MostToLeastDerivedPlaceTypeComparator implements Comparator<JClassType> {
    public MostToLeastDerivedPlaceTypeComparator() {
    }

    public int compare(JClassType o1, JClassType o2) {
        return o1.equals(o2) ? 0 : ComparisonChain.start().compare(o2.getFlattenedSupertypeHierarchy().size(), o1.getFlattenedSupertypeHierarchy().size()).compare(o1.getQualifiedSourceName(), o2.getQualifiedSourceName()).result();
    }
}