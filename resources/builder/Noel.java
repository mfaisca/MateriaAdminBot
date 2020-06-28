package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Noel extends _Default{
	public Noel() {
		super("Noel", null);
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{7780};
				break;
			case HP:
				ids = new int[]{4779, 4672};
				break;
			case S1:
				ids = new int[]{4664, 4663};
				break;
			case S2:
				ids = new int[]{4778, 4668};
				break;
			case EX:
				ids = new int[]{7779, 7778};
				break;
			case AA:
				ids = new int[]{4680};
				break;
			case LD:
				ids = new int[]{};
				break;
			case BT:
				ids = new int[]{};
				break;
			case CA:
				ids = new int[]{};
				break;
		}
		if(ids.length == 0)
			return super.getAbility(type);
		else
			return IntStream.of(ids).boxed().map(i -> this.getSpecificAbility(i)).collect(Collectors.toList());
	}
}