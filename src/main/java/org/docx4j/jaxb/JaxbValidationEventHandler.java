/*
 *  Copyright 2007-2008, Plutext Pty Ltd.
 *   
 *  This file is part of docx4j.

    docx4j is licensed under the Apache License, Version 2.0 (the "License"); 
    you may not use this file except in compliance with the License. 

    You may obtain a copy of the License at 

        http://www.apache.org/licenses/LICENSE-2.0 

    Unless required by applicable law or agreed to in writing, software 
    distributed under the License is distributed on an "AS IS" BASIS, 
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
    See the License for the specific language governing permissions and 
    limitations under the License.

 */

package org.docx4j.jaxb;

import java.io.IOException;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.docx4j.XmlUtils;
import org.w3c.dom.Node;

/*
 * <BSI-CUSTOMIZED>
 * This class replaces the original class provided by docx4j. It ignores any exceptions caused by
 * elements defined in Microsoft's drawing extension namespace (http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing)
 *
 * All changes are enclosed by a BSI-CUSTOMIZED tag and the original class is removed from docx4j-nightly-20120105.jar
 *
 * 2013-02-13, abr
 * </BSI-CUSTOMIZED>
 */
public class JaxbValidationEventHandler implements 
ValidationEventHandler{
    
	private static Logger log = LoggerFactory.getLogger(JaxbValidationEventHandler.class);		
	
	private boolean shouldContinue = true;
	public void setContinue(boolean val) {
		shouldContinue = val;
	}
	
	public final static String UNEXPECTED_MC_ALTERNATE_CONTENT = "unexpected element (uri:\"http://schemas.openxmlformats.org/markup-compatibility/2006\", local:\"AlternateContent\")";

    // <BSI-CUSTOMIZED>
    public final static String MICROSOFT_DRAWING_EXTENSION_NS = "http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing";
    // </BSI-CUSTOMIZED>
	
	static Templates mcPreprocessorXslt;	
	
	public static Templates getMcPreprocessor() throws IOException, TransformerConfigurationException {
		
		if (mcPreprocessorXslt==null) {
			try {
				Source xsltSource  = new StreamSource(
						org.docx4j.utils.ResourceUtils.getResource(
								"org/docx4j/jaxb/mc-preprocessor.xslt"));
				mcPreprocessorXslt = XmlUtils.getTransformerTemplate(xsltSource);
			} catch (IOException e) {
				log.error("Problem setting up  mc-preprocessor.xslt", e);
				throw(e);
			} catch (TransformerConfigurationException e) {
				log.error("Problem setting up  mc-preprocessor.xslt", e);
				throw(e);
			}
		}
		
		return mcPreprocessorXslt;
		
	}
	
    public boolean handleEvent(ValidationEvent ve) {            
      // <BSI-CUSTOMIZED>
      boolean shouldContinueLocal = false;
      Node node = ve.getLocator().getNode();
      if (node != null) {
        if (MICROSOFT_DRAWING_EXTENSION_NS.equals(node.getNamespaceURI())) {
          shouldContinueLocal = true;
        }
      }
      // </BSI-CUSTOMIZED>
		
      if (ve.getSeverity()==ValidationEvent.FATAL_ERROR 
       || ve.getSeverity()==ValidationEvent.ERROR){
    	  
          ValidationEventLocator  locator = ve.getLocator();
          
          //print message from validation event
          if (log.isDebugEnabled() || ve.getMessage().length() < 120 ) {
        	  log.warn( printSeverity(ve) + ": " + ve.getMessage() );
          } else {
        	  /* These messages are long, for example:
        	   * 
        	   *    unexpected element (uri:"http://schemas.openxmlformats.org/wordprocessingml/2006/main", local:"style"). 
        	   *    Expected elements are <{http://schemas.openxmlformats.org/wordprocessingml/2006/main}annotationRef>,
        	   *    ....
        	   *  
        	   * so truncate it.
        	   */
        	  log.warn( printSeverity(ve) + ": " + ve.getMessage().substring(0, 120));        	  
          }
          
          if (ve.getLinkedException()!=null && log.isDebugEnabled() ) {
              ve.getLinkedException().printStackTrace();        	   
          }
          
          //output line and column number
//          System.out.println("Column is " + 
//                locator.getColumnNumber() + 
//                " at line number " + locator.getLineNumber());
       } else if (ve.getSeverity()==ve.WARNING) {
    	   log.warn(printSeverity(ve) + "Message is " + ve.getMessage());    	   
       }
      // JAXB provider should attempt to continue its current operation. 
      // (Marshalling, Unmarshalling, Validating)
      log.info("continuing (with possible element/attribute loss)");
      return shouldContinue
        // <BSI-CUSTOMIZED>
          || shouldContinueLocal;
        // </BSI-CUSTOMIZED>
     }
    
    public String printSeverity(ValidationEvent ve) {
    	
    	String errorLevel;
    	
    	switch (ve.getSeverity()) {
    		case ValidationEvent.FATAL_ERROR: { 
              	// FATAL_ERROR is usually not actually fatal! 
    			errorLevel="(non)FATAL_ERROR"; 
    			break; } 
    		case ValidationEvent.ERROR: { errorLevel="ERROR"; break; }
    		case ValidationEvent.WARNING: { errorLevel="WARNING"; break; }
    		default: errorLevel = new Integer (ve.getSeverity()).toString() ;
    	}
    	
    	return "[" + errorLevel + "] ";
    	
    }
  
 }
