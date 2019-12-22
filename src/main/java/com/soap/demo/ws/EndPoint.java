package com.soap.demo.ws;


import com.javaspringclub.gs_ws.*;
import com.soap.demo.entity.MovieEntity;
import com.soap.demo.repo.MoviesRepository;
import com.soap.demo.service.MovieEntityService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class EndPoint {

    public static final String NAMESPACE = "http://www.soap.demo.ws.com/movies-ws";

    private MoviesRepository repository;
    private MovieEntityService service;

    @Autowired
    public EndPoint(MoviesRepository repository, MovieEntityService service) {
        this.repository = repository;
        this.service = service;
    }


    @PayloadRoot(namespace = NAMESPACE, localPart = "getMovieByIdRequest")
    @ResponsePayload
    public GetMovieByIdResponse getMovieById (@RequestPayload GetMovieByIdRequest request) {
        GetMovieByIdResponse response = new GetMovieByIdResponse();
        MovieEntity movie = repository.findById(request.getMovieId()).get();
        MovieType movieType = new MovieType();
        BeanUtils.copyProperties(movie, movieType);
        response.setMovieType(movieType);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "addMovieRequest")
    @ResponsePayload
    public AddMovieResponse addMovie(@RequestPayload AddMovieRequest request) {
        AddMovieResponse response = new AddMovieResponse();
        MovieEntity requestMovie = new MovieEntity(request.getTitle(), request.getCategory());
        MovieType movieType = new MovieType();
        ServiceStatus status = new ServiceStatus();

        MovieEntity responseMovie = service.addEntity(requestMovie);
        if (responseMovie == null) {
            status.setStatusCode("CONFLICT");
            status.setMessage("Exception while adding entity");
        } else {
            status.setStatusCode("SUCCESS");
            status.setMessage("Movie added successfully!");
            BeanUtils.copyProperties(responseMovie, movieType);
        }
        response.setMovieType(movieType);
        response.setServiceStatus(status);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "deleteMovieRequest")
    @ResponsePayload
    public DeleteMovieResponse deleteMovie(@RequestPayload DeleteMovieRequest request) {
        DeleteMovieResponse response = new DeleteMovieResponse();
        ServiceStatus status = new ServiceStatus();
        Long movieId = request.getMovieId();

        boolean isDeleted = service.deleteEntityById(movieId);
        if (isDeleted) {
            status.setStatusCode("SUCCESS");
            status.setMessage("Movie deleted successfully");
        } else {
            status.setStatusCode("FAIL");
            status.setMessage("Exception while deleting Entity id=" + request.getMovieId());
        }
        response.setServiceStatus(status);
        return response;
    }

}
