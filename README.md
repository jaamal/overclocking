# Motivation

Nowadays search algorithms on huge data sets attract much attention. One of key approaches of such algorithms is to compute compressed representation of input data and to execute search queries on compressed representation. So it save a storage's space and reduce size of input.

Various compressed representations are known: **straight-line programs (SLPs)**, collage-systems, string representations using antidictionaries, etc. Text compression based on context-free grammars such as SLPs is very popular. The reason for this is not only that grammars provide well-structured compression but also that the SLP-based compression is in a sense polynomially equivalent to the compression achieved by the Lempel-Ziv algorithm that is widely used in practice. It means that, given a text S, there is a polynomial relation between the size of an SLP that derives S and the size of the dictionary stored by the Lempel-Ziv algorithm. It should also be noted that classical **LZ78** and **LZW** algorithms are special cases of grammar compression. In the same time other compression algorithms from Lempel-Ziv family (such as **LZ77** and **run-length compression**) do not fit directly into grammar compression model.

But there is a price to pay: some classical search problems become computationally hard when one deals with compressed data and measures algorithms' speed in terms of the size of compressed representations. For example, **Hamming distance**, **Literal shuffle** and **Longest common subsequence** (version of problem that both input strings are presented via SLPs). On the other hand, there exist problems that admit algorithms working rather well on compressed representations: **Pattern matching**, **Longest common substring**, **Computing all palindromes**, **Longest common subsequence** (version of problem that single input string is presented via SLP). This dichotomy gives rise to the following questions: How fast polynomially hard problems on SLPs can be solved (depends on string version of a problem)? 

# Goals

* To implement various compression and search algorithms 
* To compare the algoritms in similar environment
* To collect statistics on different inputs like DNA, random strings...
