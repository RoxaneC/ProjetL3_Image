package code;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.Arrays;

/**
 * Codes des filtres utilisés
 * 
 * @author Roxane Cellier
 * @version 2.0
 *
 */
public class Filtres {


	/**
	* Applique un filre de Sobel Y 3*3 sur chaque pixel de l'image
	*
	* @param img L'image sur laquelle appliquer le filtre
	* @return L'image après l'application du filtre
	*/
	public static BufferedImage filtreSobelY(BufferedImage img){

		/*
		 * Valeurs du filtre de Sobel horizontal utilisé ici
		 */
		float[] filtreSobelY = {	1, 2, 1,
									0, 0, 0,
									-1, -2, -1};
		
		/*
		 * Definition du noyau du filtre de Sobel 3*3 avec les valeurs
		 * décrites
		 */
		Kernel sobelY = new Kernel(3, 3, filtreSobelY) ;
		
		/*
		 * Crée un objet de convolution avec le noyau du filtre de
		 * Sobel y
		 */
		ConvolveOp convolution = new ConvolveOp(sobelY);
		
		/*
		 * Effectue l'operation de convolution de l'image img par le filtre
		 * contenu dans l'objet convolution
		 */
		BufferedImage resultat = convolution.filter(img, null);
		
		return resultat;
	}

	/**
	* Applique un filre de flou Gausséen 15*15 sur chaque pixel
	* de l'image
	*
	* @param img L'image sur laquelle appliquer le filtre
	* @return L'image après l'application du filtre
	*/
	public static BufferedImage flouGaussien(BufferedImage img){
		
		/*
		 * Valeurs du filtre Gaussien utilisé ici
		 */
		float[] filtreGaussien = {	(float) 1/273,(float) 4/273,(float) 7/273,(float) 4/273,(float) 1/273,
									(float) 4/273,(float) 16/273,(float) 26/273,(float) 16/273,(float) 4/273,
									(float) 7/273,(float) 26/273,(float) 41/273,(float) 26/273,(float) 7/273,
									(float) 4/273,(float) 16/273,(float) 26/273,(float) 16/273,(float) 4/273,
									(float) 1/273,(float) 4/273,(float) 7/273,(float) 4/273,(float) 1/273};
		
		/*
		 * Definition du noyau du filtre Gaussien 5*5 avec les valeurs
		 * décrites
		 */
		Kernel gaussien = new Kernel(5, 5, filtreGaussien) ;
		
		/*
		 * Crée un objet de convolution avec le noyau du filtre Gaussien
		 */
		ConvolveOp convolution = new ConvolveOp(gaussien);
		
		/*
		 * Effectue l'operation de convolution de l'image img par le filtre
		 * contenu dans l'objet convolution
		 */
		BufferedImage resultat = convolution.filter(img, null);
		
		return resultat;
	}

	/**
	* Applique un filtre median 3*3 sur chaque pixel de l'image
	*
	* @param img L'image sur laquelle appliquer le filtre
	* @return L'image après l'application du filtre
	*/
	public static BufferedImage filtreMedian(BufferedImage img){
		int largeur = img.getWidth();
		int longueur = img.getHeight();
		
		BufferedImage resultat = new BufferedImage(largeur, longueur, BufferedImage.TYPE_4BYTE_ABGR);

		/*
		* Permet de stocker pour chaque pixel les valeurs de son voisinage
		*/
		int[] voisinage = new int[9];

		/*
		 * Parcours de l'image
		 */
		for(int colonne = 1 ; colonne < largeur-1 ; colonne++) {
			for(int ligne = 1 ; ligne < longueur-1 ; ligne++) {

				/*
				* On récupère les valeurs des pixels du voisinage 3*3
				*/
				voisinage[0] = img.getRGB(colonne-1, ligne-1);
				voisinage[1] = img.getRGB(colonne, ligne-1);
				voisinage[2] = img.getRGB(colonne+1, ligne-1);
				voisinage[3] = img.getRGB(colonne-1, ligne);
				voisinage[4] = img.getRGB(colonne, ligne);
				voisinage[5] = img.getRGB(colonne+1, ligne);
				voisinage[6] = img.getRGB(colonne-1, ligne+1);
				voisinage[7] = img.getRGB(colonne, ligne+1);
				voisinage[8] = img.getRGB(colonne+1, ligne+1);

				/*
				 * Tri les valeurs du voisinage dans l'ordre croissant
				 */
				Arrays.sort(voisinage);

				/*
				* Donne au pixel centré la valeur medianne du voisinage
				*/
				resultat.setRGB(colonne, ligne, voisinage[4]);
			}
		}
		
		return resultat;
	}

	/**
	* Applique une fermeture de noyau 7*7 sur chaque pixel
	* de l'image (DILATATION + EROSION)
	*
	* @param img L'image sur laquelle appliquer le filtre
	* @return L'image après l'application d'une femreture
	*/ 
	public static BufferedImage fermeture(BufferedImage img){

		BufferedImage dilate = dilatation(img);
		BufferedImage resultat = erosion(dilate);
		
		return resultat;
	}

	
	
	//METHODES PRiVÉES UTILISÉES
	/**
	 * Effectue une dilatation sur l'image
	 * 
	 * @param img L'image sur laquelle appliquer la dilatation
	 * @return L'image dilatée
	 */
	private static BufferedImage dilatation(BufferedImage img) {
		
		int largeur = img.getWidth();
		int longueur = img.getHeight();
		
		BufferedImage resultat = new BufferedImage(largeur, longueur, BufferedImage.TYPE_4BYTE_ABGR);
				
		/*
		 * Parcour de l'image
		 */
		for(int colonne = 3 ; colonne < largeur-3 ; colonne++) {
			for(int ligne = 3 ; ligne < longueur-3 ; ligne++) {
				
				/*
				 * Verifie si les pixels du voisinage 7*7 de chaque pixel de l'image
				 * sont differents de noir, et stocke cette valeur dans un bouléen
				 */
				/*
				 * Première ligne
				 */
				Boolean a1 = (img.getRGB(colonne-3, ligne-3) != Color.black.getRGB());
				Boolean a2 = (img.getRGB(colonne-2, ligne-3) != Color.black.getRGB());
				Boolean a3 = (img.getRGB(colonne-1, ligne-3) != Color.black.getRGB());
				Boolean a4 = (img.getRGB(colonne, ligne-3) != Color.black.getRGB());
				Boolean a5 = (img.getRGB(colonne+1, ligne-3) != Color.black.getRGB());
				Boolean a6 = (img.getRGB(colonne+2, ligne-3) != Color.black.getRGB());
				Boolean a7 = (img.getRGB(colonne+3, ligne-3) != Color.black.getRGB());
				/*
				 * Deuxième ligne
				 */
				Boolean b1 = (img.getRGB(colonne-3, ligne-2) != Color.black.getRGB());
				Boolean b2 = (img.getRGB(colonne-2, ligne-2) != Color.black.getRGB());
				Boolean b3 = (img.getRGB(colonne-1, ligne-2) != Color.black.getRGB());
				Boolean b4 = (img.getRGB(colonne, ligne-2) != Color.black.getRGB());
				Boolean b5 = (img.getRGB(colonne+1, ligne-2) != Color.black.getRGB());
				Boolean b6 = (img.getRGB(colonne+2, ligne-2) != Color.black.getRGB());
				Boolean b7 = (img.getRGB(colonne+3, ligne-2) != Color.black.getRGB());
				/*
				 * Troisième ligne
				 */
				Boolean c1 = (img.getRGB(colonne-3, ligne-1) != Color.black.getRGB());
				Boolean c2 = (img.getRGB(colonne-2, ligne-1) != Color.black.getRGB());
				Boolean c3 = (img.getRGB(colonne-1, ligne-1) != Color.black.getRGB());
				Boolean c4 = (img.getRGB(colonne, ligne-1) != Color.black.getRGB());
				Boolean c5 = (img.getRGB(colonne+1, ligne-1) != Color.black.getRGB());
				Boolean c6 = (img.getRGB(colonne+2, ligne-1) != Color.black.getRGB());
				Boolean c7 = (img.getRGB(colonne+3, ligne-1) != Color.black.getRGB());
				/*
				 * Quatrième ligne
				 */
				Boolean d1 = (img.getRGB(colonne-3, ligne) != Color.black.getRGB());
				Boolean d2 = (img.getRGB(colonne-2, ligne) != Color.black.getRGB());
				Boolean d3 = (img.getRGB(colonne-1, ligne) != Color.black.getRGB());
				Boolean d4 = (img.getRGB(colonne, ligne) != Color.black.getRGB());
				Boolean d5 = (img.getRGB(colonne+1, ligne) != Color.black.getRGB());
				Boolean d6 = (img.getRGB(colonne+2, ligne) != Color.black.getRGB());
				Boolean d7 = (img.getRGB(colonne+3, ligne) != Color.black.getRGB());
				/*
				 * Cinquième ligne
				 */
				Boolean e1 = (img.getRGB(colonne-3, ligne+1) != Color.black.getRGB());
				Boolean e2 = (img.getRGB(colonne-2, ligne+1) != Color.black.getRGB());
				Boolean e3 = (img.getRGB(colonne-1, ligne+1) != Color.black.getRGB());
				Boolean e4 = (img.getRGB(colonne, ligne+1) != Color.black.getRGB());
				Boolean e5 = (img.getRGB(colonne+1, ligne+1) != Color.black.getRGB());
				Boolean e6 = (img.getRGB(colonne+2, ligne+1) != Color.black.getRGB());
				Boolean e7 = (img.getRGB(colonne+3, ligne+1) != Color.black.getRGB());
				/*
				 * Sixième ligne
				 */
				Boolean f1 = (img.getRGB(colonne-3, ligne+2) != Color.black.getRGB());
				Boolean f2 = (img.getRGB(colonne-2, ligne+2) != Color.black.getRGB());
				Boolean f3 = (img.getRGB(colonne-1, ligne+2) != Color.black.getRGB());
				Boolean f4 = (img.getRGB(colonne, ligne+2) != Color.black.getRGB());
				Boolean f5 = (img.getRGB(colonne+1, ligne+2) != Color.black.getRGB());
				Boolean f6 = (img.getRGB(colonne+2, ligne+2) != Color.black.getRGB());
				Boolean f7 = (img.getRGB(colonne+3, ligne+2) != Color.black.getRGB());
				/*
				 * Septième ligne
				 */
				Boolean g1 = (img.getRGB(colonne-3, ligne+3) != Color.black.getRGB());
				Boolean g2 = (img.getRGB(colonne-2, ligne+3) != Color.black.getRGB());
				Boolean g3 = (img.getRGB(colonne-1, ligne+3) != Color.black.getRGB());
				Boolean g4 = (img.getRGB(colonne, ligne+3) != Color.black.getRGB());
				Boolean g5 = (img.getRGB(colonne+1, ligne+3) != Color.black.getRGB());
				Boolean g6 = (img.getRGB(colonne+2, ligne+3) != Color.black.getRGB());
				Boolean g7 = (img.getRGB(colonne+3, ligne+3) != Color.black.getRGB());
				
				/*
				 * Si l'un des pixel dans le voisinnage est allumé, alors le pixel
				 * central est allumé, sinon il s'éteind.
				 */
				if (	a1 || a2 || a3 || a4 || a5 || a6 || a7 ||
						b1 || b2 || b3 || b4 || b5 || b6 || b7 ||
						c1 || c2 || c3 || c4 || c5 || c6 || c7 ||
						d1 || d2 || d3 || d4 || d5 || d6 || d7 ||
						e1 || e2 || e3 || e4 || e5 || e6 || e7 ||
						f1 || f2 || f3 || f4 || f5 || f6 || f7 ||
						g1 || g2 || g3 || g4 || g5 || g6 || g7) {
					resultat.setRGB(colonne, ligne, Color.white.getRGB());
				} else {
					resultat.setRGB(colonne, ligne, Color.black.getRGB());
				}
			}
		}
		return resultat;
	}
	
	/**
	 * Effectue une erosion sur l'image
	 * 
	 * @param img L'image sur laquelle appliquer l'erosion
	 * @return L'image érodée
	 */
	private static BufferedImage erosion(BufferedImage img) {

		int largeur = img.getWidth();
		int longueur = img.getHeight();
		
		BufferedImage resultat = new BufferedImage(largeur, longueur, BufferedImage.TYPE_4BYTE_ABGR);
		
		/*
		 * Parcour de l'image
		 */
		for(int colonne = 3 ; colonne < largeur-3 ; colonne++) {
			for(int ligne = 3 ; ligne < longueur-3 ; ligne++) {
				
				/*
				 * Verifie si les pixels du voisinage 7*7 de chaque pixel de l'image
				 * sont differents de noir, et stocke cette valeur dans un bouléen
				 */
				/*
				 * Première ligne
				 */
				Boolean a1 = (img.getRGB(colonne-3, ligne-3) != Color.black.getRGB());
				Boolean a2 = (img.getRGB(colonne-2, ligne-3) != Color.black.getRGB());
				Boolean a3 = (img.getRGB(colonne-1, ligne-3) != Color.black.getRGB());
				Boolean a4 = (img.getRGB(colonne, ligne-3) != Color.black.getRGB());
				Boolean a5 = (img.getRGB(colonne+1, ligne-3) != Color.black.getRGB());
				Boolean a6 = (img.getRGB(colonne+2, ligne-3) != Color.black.getRGB());
				Boolean a7 = (img.getRGB(colonne+3, ligne-3) != Color.black.getRGB());
				/*
				 * Deuxième ligne
				 */
				Boolean b1 = (img.getRGB(colonne-3, ligne-2) != Color.black.getRGB());
				Boolean b2 = (img.getRGB(colonne-2, ligne-2) != Color.black.getRGB());
				Boolean b3 = (img.getRGB(colonne-1, ligne-2) != Color.black.getRGB());
				Boolean b4 = (img.getRGB(colonne, ligne-2) != Color.black.getRGB());
				Boolean b5 = (img.getRGB(colonne+1, ligne-2) != Color.black.getRGB());
				Boolean b6 = (img.getRGB(colonne+2, ligne-2) != Color.black.getRGB());
				Boolean b7 = (img.getRGB(colonne+3, ligne-2) != Color.black.getRGB());
				/*
				 * Troisième ligne
				 */
				Boolean c1 = (img.getRGB(colonne-3, ligne-1) != Color.black.getRGB());
				Boolean c2 = (img.getRGB(colonne-2, ligne-1) != Color.black.getRGB());
				Boolean c3 = (img.getRGB(colonne-1, ligne-1) != Color.black.getRGB());
				Boolean c4 = (img.getRGB(colonne, ligne-1) != Color.black.getRGB());
				Boolean c5 = (img.getRGB(colonne+1, ligne-1) != Color.black.getRGB());
				Boolean c6 = (img.getRGB(colonne+2, ligne-1) != Color.black.getRGB());
				Boolean c7 = (img.getRGB(colonne+3, ligne-1) != Color.black.getRGB());
				/*
				 * Quatrième ligne
				 */
				Boolean d1 = (img.getRGB(colonne-3, ligne) != Color.black.getRGB());
				Boolean d2 = (img.getRGB(colonne-2, ligne) != Color.black.getRGB());
				Boolean d3 = (img.getRGB(colonne-1, ligne) != Color.black.getRGB());
				Boolean d4 = (img.getRGB(colonne, ligne) != Color.black.getRGB());
				Boolean d5 = (img.getRGB(colonne+1, ligne) != Color.black.getRGB());
				Boolean d6 = (img.getRGB(colonne+2, ligne) != Color.black.getRGB());
				Boolean d7 = (img.getRGB(colonne+3, ligne) != Color.black.getRGB());
				/*
				 * Cinquième ligne
				 */
				Boolean e1 = (img.getRGB(colonne-3, ligne+1) != Color.black.getRGB());
				Boolean e2 = (img.getRGB(colonne-2, ligne+1) != Color.black.getRGB());
				Boolean e3 = (img.getRGB(colonne-1, ligne+1) != Color.black.getRGB());
				Boolean e4 = (img.getRGB(colonne, ligne+1) != Color.black.getRGB());
				Boolean e5 = (img.getRGB(colonne+1, ligne+1) != Color.black.getRGB());
				Boolean e6 = (img.getRGB(colonne+2, ligne+1) != Color.black.getRGB());
				Boolean e7 = (img.getRGB(colonne+3, ligne+1) != Color.black.getRGB());
				/*
				 * Sixième ligne
				 */
				Boolean f1 = (img.getRGB(colonne-3, ligne+2) != Color.black.getRGB());
				Boolean f2 = (img.getRGB(colonne-2, ligne+2) != Color.black.getRGB());
				Boolean f3 = (img.getRGB(colonne-1, ligne+2) != Color.black.getRGB());
				Boolean f4 = (img.getRGB(colonne, ligne+2) != Color.black.getRGB());
				Boolean f5 = (img.getRGB(colonne+1, ligne+2) != Color.black.getRGB());
				Boolean f6 = (img.getRGB(colonne+2, ligne+2) != Color.black.getRGB());
				Boolean f7 = (img.getRGB(colonne+3, ligne+2) != Color.black.getRGB());
				/*
				 * Septième ligne
				 */
				Boolean g1 = (img.getRGB(colonne-3, ligne+3) != Color.black.getRGB());
				Boolean g2 = (img.getRGB(colonne-2, ligne+3) != Color.black.getRGB());
				Boolean g3 = (img.getRGB(colonne-1, ligne+3) != Color.black.getRGB());
				Boolean g4 = (img.getRGB(colonne, ligne+3) != Color.black.getRGB());
				Boolean g5 = (img.getRGB(colonne+1, ligne+3) != Color.black.getRGB());
				Boolean g6 = (img.getRGB(colonne+2, ligne+3) != Color.black.getRGB());
				Boolean g7 = (img.getRGB(colonne+3, ligne+3) != Color.black.getRGB());
				
				/*
				 * Si tous les pixels dans le voisinnage sont allumés, alors le pixel
				 * central est allumé, sinon il s'éteind.
				 */
				if (	a1 && a2 && a3 && a4 && a5 && a6 && a7 &&
						b1 && b2 && b3 && b4 && b5 && b6 && b7 &&
						c1 && c2 && c3 && c4 && c5 && c6 && c7 &&
						d1 && d2 && d3 && d4 && d5 && d6 && d7 &&
						e1 && e2 && e3 && e4 && e5 && e6 && e7 &&
						f1 && f2 && f3 && f4 && f5 && f6 && f7 &&
						g1 && g2 && g3 && g4 && g5 && g6 && g7) {
					resultat.setRGB(colonne, ligne, Color.white.getRGB());
				} else {
					resultat.setRGB(colonne, ligne, Color.black.getRGB());
				}
			}
		}
		
		return resultat;
	}
	

}
