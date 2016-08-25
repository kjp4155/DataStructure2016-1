import java.sql.ResultSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Genre, Title 을 관리하는 영화 데이터베이스.
 * 
 * MyLinkedList 를 사용해 각각 Genre와 Title에 따라 내부적으로 정렬된 상태를 유지하는 데이터베이스이다.
 */
public class MovieDB {

	//genre list. each node have sublist of movie title.
	MyLinkedList<Genre> genreList = null;

	public MovieDB() {
		genreList = new MyLinkedList<Genre>();
	}

	public void insert(MovieDBItem item) {
		Genre newGenre = null;
		String newMovie = null;
		
		//Check if the item's genre is already in the list(DB).
		//Add only when not found.
		newGenre = genreList.find(new Genre(item.getGenre()));
		if (newGenre == null) {
			genreList.insertSorted(new Genre(item.getGenre()));
		}

		newGenre = genreList.find(new Genre(item.getGenre()));
		
		//Check if the item's title is already in the list(DB).
		//Add only when not found.
		newMovie = newGenre.findMovie( item.getTitle() );
		if( newMovie == null ){
			newGenre.insertSortedMovie(item.getTitle());		
		}
		
	}

	public void delete(MovieDBItem item) {
		Genre newGenre = genreList.find(new Genre(item.getGenre()));
		int status = 0;
		//if item's genre is not found, do nothing.
		if (newGenre == null)
			return;
		
		//delete the movie, and if it was the last member of genre's sublist,
		//delete the genre also.
		status = newGenre.deleteMovie(item.getTitle());
		if (status == -1) {
			genreList.delete(newGenre);
		}
		
	}

	public MyLinkedList<MovieDBItem> search(String term) {
		MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();
		
		//Simply iterate every movie item, check if each item has substring term.
		//If it has term, add it to result list and return.
		for (Genre genreIter : genreList) {
			for (String movieIter : genreIter.getMovieList()) {
				if (movieIter.contains(term))
					results.insertSorted(new MovieDBItem(genreIter.getItem(), movieIter));
			}
		}

		return results;
	}

	public MyLinkedList<MovieDBItem> items() {
		MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();

		//Simply iterate every movie item, and add it to result list and return.
		for (Genre genreIter : genreList) {
			for (String movieIter : genreIter.getMovieList()) {
				results.insertSorted(new MovieDBItem(genreIter.getItem(), movieIter));
			}
		}
		return results;
	}
}


class Genre extends Node<String> implements Comparable<Genre> {

	//Sublist to save movie item. by title.
	private MyLinkedList<String> movieList = null;

	public Genre(String name) {
		super(name);
		movieList = new MyLinkedList<String>();
		this.setItem(name);
	}

	public void insertSortedMovie(String newMovieName) {
		movieList.insertSorted(newMovieName);
	}

	public int deleteMovie(String deleteMovieName) {
		movieList.delete(deleteMovieName);
		if (movieList.size() == 0)
			return -1; // when there's no movie left in this genre
		return 0; // when there's still some movies in this genre
	}
	
	public String findMovie(String findMovieName){
		return movieList.find(findMovieName);
	}

	public MyLinkedList<String> getMovieList(){
		return movieList;
	}
	
	@Override
	public int compareTo(Genre o) {
		return this.getItem().compareTo(o.getItem());
	}

	//Since we compare Genre class only with it's name(Genre name),
	//Implement hashCode with using this.getItem(), which is genre name.
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getItem() == null) ? 0 : this.getItem().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Genre other = (Genre) obj;
		if (this.getItem() == null) {
			if (other.getItem() != null)
				return false;
		} else if (!this.getItem().equals(other.getItem()))
			return false;
		return true;
	}

}
