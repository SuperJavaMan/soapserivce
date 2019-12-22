package com.soap.demo.repo;

import com.soap.demo.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoviesRepository extends JpaRepository <MovieEntity, Long >  {

    MovieEntity findByTitle (String title);
}
