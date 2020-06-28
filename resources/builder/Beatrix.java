package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Beatrix extends _Default{
	public Beatrix() {
		super("Beatrix", "bea", "bea", "bea", "bea");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{7482};
				break;
			case HP:
				ids = new int[]{7275};
				break;
			case S1:
				ids = new int[]{7260};
				break;
			case S2:
				ids = new int[]{7266, 7265};
				break;
			case EX:
				ids = new int[]{7274, 7273, 7481};
				break;
			case AA:
				ids = new int[]{7283};
				break;
			case LD:
				ids = new int[]{10361};
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