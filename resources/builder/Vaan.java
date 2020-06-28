package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Vaan extends _Default{
	public Vaan() {
		super("Vaan", "captain basch", "captain basch");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{4986, 4987};
				break;
			case HP:
				ids = new int[]{5832};
				break;
			case S1:
				ids = new int[]{4975, 4973};
				break;
			case S2:
				ids = new int[]{4979, 4976};
				break;
			case EX:
				ids = new int[]{5833, 5678, 5677};
				break;
			case AA:
				ids = new int[]{5698};
				break;
			case LD:
				ids = new int[]{9113};
				break;
			case BT:
				ids = new int[]{9112};
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