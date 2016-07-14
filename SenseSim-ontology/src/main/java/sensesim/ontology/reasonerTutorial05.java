package sensesim.ontology;

import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.PrintUtil;

import java.io.*;
import java.util.Iterator;

/**
 * Created by Michal on 2016-07-14.
 */
public class reasonerTutorial05 {

    private static String fnameschema = "owlDemoSchema.xml";
    private static String fnameinstance = "owlDemoData.xml";
    private static String NS = "urn:x-hp:eg/";


    public static void main(String args[]) {
        Model schema = FileManager.get().loadModel(fnameschema);
        Model data = FileManager.get().loadModel(fnameinstance);
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner = reasoner.bindSchema(schema);
        InfModel infmodel = ModelFactory.createInfModel(reasoner, data);
        Resource nForce = infmodel.getResource(NS+"nForce");
        RDFNode n = (RDFNode) null;
        Property p = (Property) null;
        System.out.println("nForce *:");
        printStatements(nForce, p, n, infmodel); 	}

    public static void printStatements(Resource r, Property p, RDFNode o, Model 	m)  {
        for (StmtIterator i = m.listStatements(r, p, o ); i.hasNext(); ) {
            Statement s = i.nextStatement();
            System.out.println("-" + PrintUtil.print(s)); }
    }
}
