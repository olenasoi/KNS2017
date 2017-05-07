package salesman_problem;

public class GA {

    private static final double mutationRate = 0.015;
    private static final int tournamentSize = 5;
    private static final boolean elitism = true;

    
    public static Population evolvePopulation(Population pop) {
        Population newPopulation = new Population(pop.populationSize(), false);

      
        int elitismOffset = 0;
        if (elitism) {
            newPopulation.saveTour(0, pop.getFittest());
            elitismOffset = 1;
        }

     // кросовер популяції
        for (int i = elitismOffset; i < newPopulation.populationSize(); i++) {
       
            Tour parent1 = tournamentSelection(pop);
            Tour parent2 = tournamentSelection(pop);
         
            Tour child = crossover(parent1, parent2);
  
            newPopulation.saveTour(i, child);
        }

     // мутуція нової популяції 
        for (int i = elitismOffset; i < newPopulation.populationSize(); i++) {
            mutate(newPopulation.getTour(i));
        }

        return newPopulation;
    }

    //Застосовується кросовера до набору батьків і створення потомство
    public static Tour crossover(Tour parent1, Tour parent2) {
      
        Tour child = new Tour();
        int p1 = (int) (Math.random() * parent1.tourSize());
        int p2 = (int) (Math.random() * parent1.tourSize());
        for(int i = 0; i < child.tourSize(); i++)
        {
        	if(i>=p1 && i<=p2) child.setCity(i, null);
        	else child.setCity(i, parent1.getCity(i));
        }
        int n = 0;
        for ( int j = 0; j < parent2.tourSize(); j++)
        {
        	boolean t = false;
        	for ( int k = 0; k < child.tourSize(); k++)
        	{
        		if(parent2.getCity(j) == child.getCity(k)) {
        			t = true;
        			break;
        		}
        	}
        	if (t== false){
        		child.setCity(p1+n, parent2.getCity(j));
        		n = n+1;
        	}
        }
        
        return child;
    }

    //мутація
    private static void mutate(Tour tour) {
    	
    	int tourPos1 = (int) (tour.tourSize() * Math.random());
    	int tourPos2 = (int) (tour.tourSize() * Math.random());
    	
    	if (tourPos2 < tourPos1) {
    		int q = tourPos1;
    		tourPos1 = tourPos2;
    		tourPos2 = q;
    	}
    	
    	for(int i = 0; i <= (tourPos2 - tourPos1 +1)/2 ; i++)
    	{
    		City q = tour.getCity(tourPos1+i);
    		tour.setCity(tourPos1+i, tour.getCity(tourPos2-i));
    		tour.setCity(tourPos2-i, q);
    		
    	}
    	
    	
    }
    //Вибір кандидата  для кросовера
    private static Tour  tournamentSelection(Population pop) {
        
        Population truncation = new Population(tournamentSize, false);
       
        for (int i = 0; i < tournamentSize; i++) {
            int randomId = (int) (Math.random() * pop.populationSize());
            
            truncation.saveTour(i, pop.getTour(randomId));
        }
   
        Tour fittest = truncation.getFittest();
        return fittest;
    }
}