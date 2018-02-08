Readme.txt
Gregory Muchka - 10153582
October 6 2017
CPSC 418 - Assignment #1 Question #6

Files submitted: 
- secureFile.java:  The purpose of this file is to encrypt a file (assumed to be no bigger than 1MB in size). For the message Authentication process I am using SHA-1. 
					To generate the random key I am using SHA1PRNG of type SecureRandom. 
					Encryption steps are as follow. First I read the file into a byte array. Next I computed the digest. After copying the data into a byte array, first 
					the bytes of the plaintext, followed by the digest I encrypted the entire byte array and wrote it to a file. The processes used to encrypt was 
					AES/CBC/PKCS5Padding. 
- decryptFile.java: The purpose of this file is to decrypt a file (assumed to be no bigger than 1MB in size). For the message Authentication process I am using SHA-1. 
					To generate the random key I am using SHA1PRNG of type SecureRandom. 
					Decryption steps are as follow. First I read the ciphertext into a byte array. First I decryt the entire byte array. After which I take the last
					20 bytes iff of the decrypted array (this is the digest). I recompute the digest using the decrypted cipher text (not including the previous digest).
					After this I compare thw two digests. If they are the same then the message was not altered and we still have the original message. However if there is 
					a difference then something was changed in transit.
					
Compiling instructions: No makefile is provided. The java files can be compiled using the command "javac [file name here].java. This was done on CPSC department Linux Servers
			The executable can be run using the command "java secureFile [plaintext-filename] [seed]" and "java decryptFile [ciphertext-filename] [seed]"
			For this to work the seed must be the same for both files
						
The problem has been solved in full with no known bugs or missing criteria. 
