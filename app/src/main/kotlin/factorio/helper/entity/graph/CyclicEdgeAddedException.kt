package factorio.helper.entity.graph

class CyclicEdgeAddedException : Exception("Cannot add edge which creates cycle to acyclic graph")
