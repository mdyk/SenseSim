package org.mdyk.netsim.logic.ontology;


import aterm.ATermAppl;
import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
import com.clarkparsia.pellet.sparqldl.engine.QueryEngine;
import com.clarkparsia.pellet.sparqldl.model.Query;
import com.clarkparsia.pellet.sparqldl.model.QueryResult;
import com.clarkparsia.pellet.sparqldl.model.ResultBinding;
import com.clarkparsia.pellet.sparqldl.parser.QueryParser;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.util.List;

/**
 * Created by michal on 18.08.2016.
 */
public class QueryTest {

    @Test
    public void queryTest() throws Exception {
        OWLDataFactory df = OWLManager.getOWLDataFactory();
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(FileUtils.toFile(getClass().getResource("/test-ontology.owl")));

        String ontologyIRI = "http://www.semanticweb.org/mariuszchmielewski/ontologies/2016/6/untitled-ontology-9";

        String prefix ="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX ont: <"+ontologyIRI+"#> \n";

        String queryString = "SELECT distinct ?ind \n" +
                "WHERE { ?ind rdf:type ont:Device }\n";

        QueryParser queryParser = QueryEngine.getParser();

        PelletReasoner reasoner = PelletReasonerFactory.getInstance().createReasoner( ontology );

        Query q = queryParser.parse(prefix + queryString , reasoner.getKB());
        QueryResult qr = QueryEngine.exec(q);

        List<ATermAppl> resultVars =  qr.getResultVars();

        if (qr.size() == 1 ) {
            ResultBinding resultBinding = qr.iterator().next();
            ATermAppl aTermAppl = qr.getResultVars().get(0);
            String result = resultBinding.getValue(aTermAppl).getName();
            System.out.println(result);
        }

//        for(Iterator<ResultBinding> i = qr.iterator(); i.hasNext(); ) {
//            ResultBinding mapping = i.next();
//            for( Iterator<?> j = q.getVars().iterator(); j.hasNext(); ) {
//                ATermAppl var = (ATermAppl) j.next();
//                System.out.println( var.getArgument( 0 ) + " -> " + mapping.getValue( var ).getName()); //I get var(x) as opposed to x
//                if( j.hasNext() )
//                    System.out.print( ", " );
//            }
//        }


//        for(ATermAppl aTermAppl : resultVars) {
////            ATermAppl var = (ATermAppl) j.next();
//
//            System.out.println(aTermAppl.getArgument( 0 ));
//
////            System.out.print( var.getArgument( 0 ) + " -> " + mapping.getValue( var )); //I get var(x) as opposed to x
////            if( j.hasNext() )
////                System.out.print( ", " );
//        }

    }

}
