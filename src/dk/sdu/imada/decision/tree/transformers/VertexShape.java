/*
 *            	Life-Style-Specific-Islands
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public License.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 *      http://www.gnu.org/copyleft/lesser.html
 *      
 * This material was developed as part of a research project at 
 * the University of Southern Denmark (SDU - Odense, Denmark) 
 * and the Federal University of Minas Gerais (UFMG - Belo 
 * Horizonte, Brazil). For more information please access:
 * 
 *      	https://lissi.compbio.sdu.dk/ 
 */
package dk.sdu.imada.decision.tree.transformers;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import org.apache.commons.collections15.Transformer;

/**
 * Class edits vertex size and shape.
 * 
 * @author Eudes Barbosa
 */
public class VertexShape implements Transformer<String, Shape> {

	@Override
	public Shape transform(String s) {
		Rectangle2D rect = new Rectangle2D.Double(-15, -15, 30, 30);
		// The vertex is twice as large
		return AffineTransform.getScaleInstance(2.7, 2).createTransformedShape(rect);
	}
}
