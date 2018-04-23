import re
import argparse

parser = argparse.ArgumentParser()
parser.add_argument('--infile', help="proper usage is --infile \'text file\'", type = str)
parser.add_argument('--sort', help="sorts the output by frequancy", action = "store_true")
parser.add_argument('--print-words', help="prints words", action = "store_true")
args = parser.parse_args()

ip_File = "ip.txt"
#takes the input file, splits it into words, strips it of punctuation, and makes it all lowercase
words = [((re.sub(r'[^\w\s]','',word).lower()) , len((re.sub(r'[^\w\s]','',word).lower()))) for line in open(args.infile, 'r') for word in line.split()]
word_d= {}
for items in words:
	if items[1] not in word_d:
		word_d[items[1]] = [1, [items[0]]]
	else:
		word_d[items[1]][0] = word_d[items[1]][0]+1
		word_d[items[1]][1].append(items[0])
		word_d[items[1]][1] = sorted(list(set(word_d[items[1]][1])))
print_list = []
for key in word_d:
	print_list.append([key , word_d[key][0], word_d[key][1]])

if args.sort:
	print_list = sorted(print_list, key=lambda count: count[1], reverse = True)	
else:
	print_list = sorted(print_list, key=lambda count: count[0], reverse = False)	
if not args.print_words:	
	for item in print_list:
		print("Count[{}]={}".format( item[0], item[1]))

else:
	for item in print_list:
		print("Count[{}]={}".format( item[0], item[1]), end = '', flush = True)
		print(" (words: \"{}\"".format(item[2][0]), end = '', flush = True)
		if len(item[2]) > 1:
			length = len(item[2])
			i = 1
			while i < length-1:
				print(", \"{}\"".format(item[2][i]),end ='', flush = True)
				i+=1
			print(" and \"{}\"".format(item[2][-1]),end ='', flush = True)
		print(')')















#d_words = {word_tuple:(words.count(word_tuple)) for word_tuple in words}
#final_words = []
#for keys in d_words:
		

	
