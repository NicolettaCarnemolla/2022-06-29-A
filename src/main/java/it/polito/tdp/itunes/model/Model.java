package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	ItunesDAO dao = new ItunesDAO();
	Graph<Album,DefaultWeightedEdge> grafo;
	List<Album> album;
	Map<Integer,Album> idMap;
	List<Arco> archi;
	
	
	public void creaGrafo(int N) {
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		idMap = new HashMap<>();
		album = dao.getAllAlbumswithCondition(N);
		Graphs.addAllVertices(grafo, album);
		for(Album a : album) {
			idMap.put(a.getAlbumId(), a);
		}
		for(Album a1 : album) {
			for(Album a2 : album) {
				int peso = a1.getPeso()-a2.getPeso();
				if(peso >0) {
					Graphs.addEdge(grafo, a1, a2, peso);
				}
			}
		}
		System.out.println(this.grafo.edgeSet().size());
		
	}
	
	public int getvertex() {
		return grafo.vertexSet().size();
	}
	
	public int getedges() {
		return grafo.edgeSet().size();
	}
	public List<Album> getallAlbum() {
		Collections.sort(album);
		return album;
	}
	
	public int getBilancio(Album a1){
		int bilancio = 0;
		List<DefaultWeightedEdge> EdgesIN = new ArrayList<>(this.grafo.incomingEdgesOf(a1));
		List<DefaultWeightedEdge> EdgesOUT = new ArrayList<>(this.grafo.outgoingEdgesOf(a1));
		
		for(DefaultWeightedEdge e : EdgesIN) {
			bilancio += this.grafo.getEdgeWeight(e);
		}
		
		for(DefaultWeightedEdge e : EdgesOUT) {
			bilancio -= this.grafo.getEdgeWeight(e);
		}
		
		return bilancio;
	}
	
	public List<bilancioAlbum> bilanci_successori(Album a){
		List<Album> successori = Graphs.successorListOf(grafo, a);
		List<bilancioAlbum> albumbilanci = new ArrayList<>();
		
		for(Album album : successori) {
			albumbilanci.add(new bilancioAlbum(album,getBilancio(album)));
		}
		
		Collections.sort(albumbilanci);
		return albumbilanci;
	}
}
